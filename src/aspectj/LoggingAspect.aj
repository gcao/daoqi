/*
 * Copyright 2004 Carlos Sanchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.oness.common.all.logging;

import org.aspectj.lang.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Aspect that logs method calling and exception throwing
 * using the apache commons logging package with the TRACE
 * facility.
 * Logs every method, avoiding recursion into the aspect and
 * into toString calls, which may lead to infinite recursion
 * and StackOverflowError exceptions
 *
 * @author Carlos Sanchez
 * @version $Revision: 1.1 $
 */
public aspect LoggingAspect {

    /**
     * The logging aspects should have the highest precedence.
     * This	way, the logging aspects before advice executes
     * before any of the other aspects before advice, and the
     * logging aspects after advice executes after the other
     * aspects after advice.
     * This makes the logging aspects dominate all other aspects
     */
    declare precedence : LoggingAspect, *;

    /**
     * Log every method avoiding recursion into the aspect and
     * into toString calls, which may lead to infinite recursion
     * and StackOverflowError exceptions
     */
    protected pointcut traceMethods() : (
        !cflow(execution(String *.toString()))
            && !cflow(within(LoggingAspect))
            && (execution(* *.* (..)))
            || execution(*.new (..)));

    /**
     * This advice logs method entries
     */
    before() : traceMethods() {
        Signature sig = thisJoinPointStaticPart.getSignature();
        Log log = getLog(sig);
        if (log.isTraceEnabled()) {
            log.trace(
                "Entering "
                    + fullName(sig)
                    + createParameterMessage(thisJoinPoint));
        }
    }

    /**
     * This advice logs method leavings
     */
    after() returning : traceMethods() {
        Signature sig = thisJoinPointStaticPart.getSignature();
        Log log = getLog(sig);
        if (log.isTraceEnabled()) {
            log.trace("Leaving " + fullName(sig));
        }
    }

    /**
     * This advice logs method leavings
     */
    after() throwing(Throwable ex) : traceMethods() {
        Signature sig = thisJoinPointStaticPart.getSignature();
        Log log = getLog(sig);
        if (log.isTraceEnabled()) {
            log.trace("Thrown " + fullName(sig) + "\n\t" + ex);
        }
    }

    /**
     * Find log for current class
     */
    private Log getLog(Signature sig) {
        return LogFactory.getLog(sig.getDeclaringType());
    }

    /**
     * @return String with class name + method name
     */
    private String fullName(Signature sig) {
        return "["
            + sig.getDeclaringType().getName()
            + "."
            + sig.getName()
            + "]";
    }

    /**
     * @return String with current object + arguments
     */
    private String createParameterMessage(JoinPoint joinPoint) {
        StringBuffer paramBuffer = new StringBuffer();

        /*
         * Log the current object except if the method is a getter, setter or
         * constructor
         */
        if (joinPoint.getThis() != null) {
            String name = joinPoint.getStaticPart().getSignature().getName();

            if (!(name.startsWith("get"))
                && !(name.startsWith("set"))
                && !(name.equals("<init>"))) {

                paramBuffer.append("\n\t[This: ");
                paramBuffer.append(joinPoint.getThis());
                paramBuffer.append("]");
            }
        }

        Object[] arguments = joinPoint.getArgs();
        if (arguments.length > 0) {
            paramBuffer.append("\n\t[Args: (");
            for (int length = arguments.length, i = 0; i < length; ++i) {
                Object argument = arguments[i];
                paramBuffer.append(argument);
                if (i != length - 1) {
                    paramBuffer.append(",");
                }
            }
            paramBuffer.append(")]");
        }
        return paramBuffer.toString();
    }
}