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

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.connector.read.{Batch, InputPartition, PartitionReader, PartitionReaderFactory, Scan, ScanBuilder, Statistics, SupportsReportStatistics}
import org.apache.spark.sql.types.StructType

import java.time.format.DateTimeFormatter

class YarnLogBatchScan(
    @transient table: YarnTable,
    schema: StructType,
    yarnTableConf: YarnTableConf) extends ScanBuilder
    with SupportsReportStatistics with Batch with Serializable {


  override def build(): Scan = this

  override def toBatch: Batch = this

  override def description(): String =
    s"Scan Yarn Logs From ${yarnTableConf.aggLogPath}, when agg-log is enabled"

  override def readSchema(): StructType = schema


}

class YarnApplicationLogReader(
    table: String,
    parallelism: Int,
    schema: StructType) extends PartitionReader[InternalRow] {

  private var currentRow: InternalRow = _

  private lazy val dateFmy: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


  override def next(): Boolean = {

  }
}

