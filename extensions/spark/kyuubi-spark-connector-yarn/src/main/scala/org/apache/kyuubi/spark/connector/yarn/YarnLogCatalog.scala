/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kyuubi.spark.connector.yarn

import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.analysis.NoSuchNamespaceException
import org.apache.spark.sql.connector.catalog.{Identifier, SupportsNamespaces, Table => SparkTable, TableCatalog}
import org.apache.spark.sql.util.CaseInsensitiveStringMap

class YarnLogCatalog extends TableCatalog with SupportsNamespaces with Logging{

  val databases: Array[String] = Array("yarn_log")

  val tables: Array[String] = Array("application")

  var yarnTableConf: YarnTableConf = _

  var _name: String = _

  override def name(): String = _name

  override def initialize(name: String, options: CaseInsensitiveStringMap): Unit = {
    this._name = name
    this.yarnTableConf = YarnTableConf(SparkSession.active, options)
  }

  override def listTables(namespace: Array[String]): Array[Identifier] = namespace match {
    case Array(db) if databases contains db => tables.map(Identifier.of(namespace, _))
    case _ => throw new NoSuchNamespaceException(namespace)
  }

  override def loadTable(ident: Identifier): SparkTable = (ident.namespace, ident.name) match {
    case (Array(db), table) if (databases contains db) && tables.contains(table.toLowerCase) =>

  }
}
