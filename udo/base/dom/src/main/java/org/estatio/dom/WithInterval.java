/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.dom;

import java.util.Iterator;
import java.util.SortedSet;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;

import org.estatio.dom.valuetypes.LocalDateInterval;

public interface WithInterval<T extends WithInterval<T>> extends WithStartDate {

    /**
     * The start date of the interval.
     * 
     * <p>
     * A value of <tt>null</tt> implies that the
     * {@link #getWithIntervalParent() parent}'s start date should be used. If
     * that is <tt>null</tt>, then implies 'the beginning of time'.
     */
    @Disabled
    @Optional
    public LocalDate getStartDate();

    public void setStartDate(LocalDate startDate);

    /**
     * The end date of the interval.
     * 
     * <p>
     * A value of <tt>null</tt> implies that the
     * {@link #getWithIntervalParent() parent}'s end date should be used. If
     * that is <tt>null</tt>, then implies 'the end of time'.
     */
    @Optional
    @Disabled
    public LocalDate getEndDate();

    public void setEndDate(LocalDate endDate);

    // /**
    // * The parent "owning" object, if any, that is itself a {@link
    // WithInterval}
    // * .
    // *
    // * <p>
    // * Used to determine the {@link #getEffectiveStartDate() effective start
    // * date} and {@link #getEffectiveEndDate() effective end date} when the
    // * actual {@link #getStartDate() start date} and {@link #getEndDate() end
    // * date} are <tt>null</tt> (in other words the start/end date are
    // inherited
    // * from the parent).
    // */
    // @Hidden
    // public WithInterval<?> getWithIntervalParent();

    // /**
    // * Either the {@link #getStartDate() start date}, or the
    // * {@link #getWithIntervalParent() parent}'s start date (if any).
    // */
    // @Hidden
    // public LocalDate getEffectiveStartDate();

    // /**
    // * Either the {@link #getEndDate() end date}, or the
    // * {@link #getWithIntervalParent() parent}'s end date (if any).
    // */
    // @Hidden
    // public LocalDate getEffectiveEndDate();

    @Programmatic
    public LocalDateInterval getInterval();

    @Programmatic
    public LocalDateInterval getEffectiveInterval();

    @Programmatic
    public boolean isCurrent();

    public final static class Util {
        private Util() {
        }

        // public static LocalDate effectiveStartDateOf(final WithInterval<?>
        // wi) {
        // if (wi.getStartDate() != null) {
        // return wi.getStartDate();
        // }
        // final WithInterval<?> parentWi = wi.getWithIntervalParent();
        // if (parentWi != null) {
        // return parentWi.getEffectiveStartDate();
        // }
        // return null;
        // }

        // public static LocalDate effectiveEndDateOf(final WithInterval<?> wi)
        // {
        // if (wi.getEndDate() != null) {
        // return wi.getEndDate();
        // }
        // final WithInterval<?> parentWi = wi.getWithIntervalParent();
        // if (parentWi != null) {
        // return parentWi.getEffectiveEndDate();
        // }
        // return null;
        // }

        public static <T extends WithInterval<T>> T firstElseNull(
                final SortedSet<T> roles, final Predicate<T> predicate) {
            final Iterable<T> filter = Iterables.filter(roles, predicate);
            final Iterator<T> iterator = filter.iterator();
            return iterator.hasNext() ? iterator.next() : null;
        }
    }
    
    public final static class Predicates {
        
        private Predicates(){}
        
        /**
         * A {@link Predicate} that tests whether the role's {@link WithInterval#isCurrent() current}
         * status is the specified value.
         */
        public static <T extends WithInterval<T>> Predicate<T> whetherCurrentIs(final boolean current) {
            return new Predicate<T>() {
                public boolean apply(final T candidate) {
                    return candidate != null && candidate.isCurrent() == current;
                }
            };
        }

    }
    
}
