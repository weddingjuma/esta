/*
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

import java.util.Map;

import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.query.QueryDefault;

public class FinderInteraction {
    public enum FinderMethod {
        FIRST_MATCH,
        ALL_MATCHES,
        ALL_INSTANCES,
        UNIQUE_MATCH
    }
    private QueryDefault<?> queryDefault;
    private FinderInteraction.FinderMethod finderMethod;
    public FinderInteraction(Query<?> query, FinderInteraction.FinderMethod finderMethod) {
        super();
        this.queryDefault = (QueryDefault<?>) query;
        this.finderMethod = finderMethod;
    }
    public QueryDefault<?> getQueryDefault() {
        return queryDefault;
    }
    public FinderInteraction.FinderMethod getFinderMethod() {
        return finderMethod;
    }
    public Class<?> getResultType() {
        return queryDefault.getResultType();
    }
    public String getQueryName() {
        return queryDefault.getQueryName();
    }
    public Map<String, Object> getArgumentsByParameterName() {
        return queryDefault.getArgumentsByParameterName();
    }
    public int numArgs() {
        return queryDefault.getArgumentsByParameterName().size();
    }
}