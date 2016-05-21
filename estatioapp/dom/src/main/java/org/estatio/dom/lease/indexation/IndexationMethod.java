package org.estatio.dom.lease.indexation;

import java.math.BigDecimal;

import org.estatio.dom.lease.LeaseTermFrequency;
import org.estatio.dom.utils.MathUtils;

public enum IndexationMethod {
    BASE_INDEX(false, false, true, IndexationCalculationMethod.DEFAULT),
    BASE_INDEX_ALLOW_DECREASE(true, true, true, IndexationCalculationMethod.DEFAULT),
    BASE_INDEX_NO_DECREASE_FRANCE(false, false, true, IndexationCalculationMethod.FRANCE),
    BASE_INDEX_ALLOW_DECREASE_BASE_AS_FLOOR_FRANCE(true, false, true, IndexationCalculationMethod.FRANCE),
    BASE_INDEX_ALLOW_DECREASE_FRANCE(true, true, true, IndexationCalculationMethod.FRANCE),
    LAST_KNOWN_INDEX(false, false, false, IndexationCalculationMethod.DEFAULT);

    private IndexationMethod(
            final boolean allowDecrease,
            final boolean allowDecreaseUnderBase,
            final boolean fixedBase,
            final IndexationCalculationMethod indexationCalculationMethod) {
        this.allowDecrease = allowDecrease;
        this.allowDecreaseUnderBase = allowDecreaseUnderBase;
        this.fixedBase = fixedBase;
        this.indexationCalculationMethod = indexationCalculationMethod;
    }

    private boolean allowDecrease;
    private boolean allowDecreaseUnderBase;
    private boolean fixedBase;
    private IndexationCalculationMethod indexationCalculationMethod;

    public IndexationCalculationMethod indexationCalculation() {
        return indexationCalculationMethod;
    }

    public void doInitialize(Indexable term, Indexable previous) {
        if (previous != null) {
            LeaseTermFrequency frequency = term.getFrequency();
            if (fixedBase) {
                // Base value is copied on initialisation, never updated
                term.setBaseValue(previous.getBaseValue());
                term.setBaseIndexStartDate(previous.getBaseIndexStartDate());
                if (term.getFrequency() != null) {
                    term.setNextIndexStartDate(frequency.nextDate(previous.getNextIndexStartDate()));
                    term.setEffectiveDate(frequency.nextDate(previous.getEffectiveDate()));
                }
            } else {
                term.setBaseIndexStartDate(previous.getNextIndexStartDate());
                if (term.getFrequency() != null) {
                    term.setNextIndexStartDate(frequency.nextDate(previous.getNextIndexStartDate()));
                    term.setEffectiveDate(frequency.nextDate(previous.getEffectiveDate()));
                }
            }
        }
    }

    public void doAlignBeforeIndexation(Indexable term, Indexable previous) {
        if (previous != null && !fixedBase) {
            //base value changes when previous term have been changed
            term.setBaseValue(
                    MathUtils.firstNonZero(
                            previous.getSettledValue(),
                            MathUtils.maxUsingFirstSignum(
                                    previous.getBaseValue(),
                                    previous.getIndexedValue(),
                                    previous.getEffectiveIndexedValue())));
        }
    }

    public void doAlignAfterIndexation(Indexable term, Indexable previous) {
        if (fixedBase) {

            final BigDecimal valueAllowingDecrease = MathUtils.firstNonZero(
                    term.getIndexedValue(),
                    previous == null ? null : MathUtils.firstNonZero(previous.getEffectiveIndexedValue(), previous.getIndexedValue()),
                    term.getBaseValue());

            final BigDecimal valueNotAllowingDecrease = MathUtils.maxUsingFirstSignum(
                    term.getBaseValue(),
                    term.getIndexedValue(),
                    previous == null ? null : MathUtils.firstNonZero(previous.getEffectiveIndexedValue(), previous.getIndexedValue()));

            if (allowDecrease && allowDecreaseUnderBase) {
                term.setEffectiveIndexedValue(
                        valueAllowingDecrease);

            } else {
                if (!allowDecrease) {
                    term.setEffectiveIndexedValue(
                            valueNotAllowingDecrease);
                } else {
                    // allowDecrease && !allowDecreaseUnderBase
                    term.setEffectiveIndexedValue(
                            MathUtils.maxUsingFirstSignum(
                                    term.getBaseValue(),
                                    valueAllowingDecrease)
                    );
                }
            }
        } else {
            term.setEffectiveIndexedValue(
                            MathUtils.maxUsingFirstSignum(
                                    term.getBaseValue(),
                                    term.getIndexedValue(),
                                    previous == null ? null : previous.getEffectiveIndexedValue()));
        }
    }

}