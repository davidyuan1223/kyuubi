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

import java.util
import scala.collection.JavaConverters._
import org.apache.spark.sql.connector.catalog.{SupportsRead, TableCapability, Table => SparkTable}
import org.apache.spark.sql.types.{DataType, StructField, StructType}



abstract class YarnTable(tableName: String, yarnTableConf: YarnTableConf)
  extends SparkTable with SupportsRead{

  override def name: String = tableName

  override def toString: String = s"YARNTable($name)"

  override def capabilities(): util.Set[TableCapability] = {
    Set(TableCapability.BATCH_READ).asJava
  }

  override def schema(): StructType = {
    StructType(
      columns().map[StructField]{
        column =>
          StructField(column.columnName, column.dataType, column.nullable)
      }.toArray
    )
  }


  def columns(): Seq[Column]
}

case class Column(columnName: String, dataType: DataType, nullable: Boolean)
