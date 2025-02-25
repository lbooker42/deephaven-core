# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: deephaven_core/proto/hierarchicaltable.proto
# Protobuf Python Version: 5.26.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from deephaven_core.proto import table_pb2 as deephaven__core_dot_proto_dot_table__pb2
from deephaven_core.proto import ticket_pb2 as deephaven__core_dot_proto_dot_ticket__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n,deephaven_core/proto/hierarchicaltable.proto\x12!io.deephaven.proto.backplane.grpc\x1a deephaven_core/proto/table.proto\x1a!deephaven_core/proto/ticket.proto\"\x9c\x02\n\rRollupRequest\x12I\n\x16result_rollup_table_id\x18\x01 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12\x42\n\x0fsource_table_id\x18\x02 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12\x44\n\x0c\x61ggregations\x18\x03 \x03(\x0b\x32..io.deephaven.proto.backplane.grpc.Aggregation\x12\x1c\n\x14include_constituents\x18\x04 \x01(\x08\x12\x18\n\x10group_by_columns\x18\x05 \x03(\t\"\x10\n\x0eRollupResponse\"\xf0\x01\n\x0bTreeRequest\x12G\n\x14result_tree_table_id\x18\x01 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12\x42\n\x0fsource_table_id\x18\x02 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12\x19\n\x11identifier_column\x18\x03 \x01(\t\x12 \n\x18parent_identifier_column\x18\x04 \x01(\t\x12\x17\n\x0fpromote_orphans\x18\x05 \x01(\x08\"\x0e\n\x0cTreeResponse\"\x9d\x01\n\x11UpdateViewRequest\x12\x42\n\x0b\x63olumn_spec\x18\x01 \x01(\x0b\x32-.io.deephaven.proto.backplane.grpc.Selectable\x12\x44\n\tnode_type\x18\x02 \x01(\x0e\x32\x31.io.deephaven.proto.backplane.grpc.RollupNodeType\"\xd9\x03\n\x1dHierarchicalTableApplyRequest\x12O\n\x1cresult_hierarchical_table_id\x18\x01 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12N\n\x1binput_hierarchical_table_id\x18\x02 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12=\n\x07\x66ilters\x18\x03 \x03(\x0b\x32,.io.deephaven.proto.backplane.grpc.Condition\x12@\n\x05sorts\x18\x04 \x03(\x0b\x32\x31.io.deephaven.proto.backplane.grpc.SortDescriptor\x12J\n\x0c\x66ormat_views\x18\x05 \x03(\x0b\x32\x34.io.deephaven.proto.backplane.grpc.UpdateViewRequest\x12J\n\x0cupdate_views\x18\x06 \x03(\x0b\x32\x34.io.deephaven.proto.backplane.grpc.UpdateViewRequest\" \n\x1eHierarchicalTableApplyResponse\"I\n\x1bHierarchicalTableDescriptor\x12\x17\n\x0fsnapshot_schema\x18\x01 \x01(\x0c\x12\x11\n\tis_static\x18\x02 \x01(\x08\"\xde\x02\n\x1cHierarchicalTableViewRequest\x12\x41\n\x0eresult_view_id\x18\x01 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12J\n\x15hierarchical_table_id\x18\x02 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.TicketH\x00\x12\x45\n\x10\x65xisting_view_id\x18\x03 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.TicketH\x00\x12^\n\nexpansions\x18\x04 \x01(\x0b\x32J.io.deephaven.proto.backplane.grpc.HierarchicalTableViewKeyTableDescriptorB\x08\n\x06target\"\xac\x01\n\'HierarchicalTableViewKeyTableDescriptor\x12?\n\x0ckey_table_id\x18\x01 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12$\n\x17key_table_action_column\x18\x02 \x01(\tH\x00\x88\x01\x01\x42\x1a\n\x18_key_table_action_column\"\x1f\n\x1dHierarchicalTableViewResponse\"\xb4\x01\n$HierarchicalTableSourceExportRequest\x12\x42\n\x0fresult_table_id\x18\x01 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket\x12H\n\x15hierarchical_table_id\x18\x02 \x01(\x0b\x32).io.deephaven.proto.backplane.grpc.Ticket*I\n\x0eRollupNodeType\x12\x16\n\x12TYPE_NOT_SPECIFIED\x10\x00\x12\x0e\n\nAGGREGATED\x10\x01\x12\x0f\n\x0b\x43ONSTITUENT\x10\x02\x32\xa9\x05\n\x18HierarchicalTableService\x12m\n\x06Rollup\x12\x30.io.deephaven.proto.backplane.grpc.RollupRequest\x1a\x31.io.deephaven.proto.backplane.grpc.RollupResponse\x12g\n\x04Tree\x12..io.deephaven.proto.backplane.grpc.TreeRequest\x1a/.io.deephaven.proto.backplane.grpc.TreeResponse\x12\x8c\x01\n\x05\x41pply\x12@.io.deephaven.proto.backplane.grpc.HierarchicalTableApplyRequest\x1a\x41.io.deephaven.proto.backplane.grpc.HierarchicalTableApplyResponse\x12\x89\x01\n\x04View\x12?.io.deephaven.proto.backplane.grpc.HierarchicalTableViewRequest\x1a@.io.deephaven.proto.backplane.grpc.HierarchicalTableViewResponse\x12\x99\x01\n\x0c\x45xportSource\x12G.io.deephaven.proto.backplane.grpc.HierarchicalTableSourceExportRequest\x1a@.io.deephaven.proto.backplane.grpc.ExportedTableCreationResponseBMH\x01P\x01ZGgithub.com/deephaven/deephaven-core/go/internal/proto/hierarchicaltableb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'deephaven_core.proto.hierarchicaltable_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'H\001P\001ZGgithub.com/deephaven/deephaven-core/go/internal/proto/hierarchicaltable'
  _globals['_ROLLUPNODETYPE']._serialized_start=2205
  _globals['_ROLLUPNODETYPE']._serialized_end=2278
  _globals['_ROLLUPREQUEST']._serialized_start=153
  _globals['_ROLLUPREQUEST']._serialized_end=437
  _globals['_ROLLUPRESPONSE']._serialized_start=439
  _globals['_ROLLUPRESPONSE']._serialized_end=455
  _globals['_TREEREQUEST']._serialized_start=458
  _globals['_TREEREQUEST']._serialized_end=698
  _globals['_TREERESPONSE']._serialized_start=700
  _globals['_TREERESPONSE']._serialized_end=714
  _globals['_UPDATEVIEWREQUEST']._serialized_start=717
  _globals['_UPDATEVIEWREQUEST']._serialized_end=874
  _globals['_HIERARCHICALTABLEAPPLYREQUEST']._serialized_start=877
  _globals['_HIERARCHICALTABLEAPPLYREQUEST']._serialized_end=1350
  _globals['_HIERARCHICALTABLEAPPLYRESPONSE']._serialized_start=1352
  _globals['_HIERARCHICALTABLEAPPLYRESPONSE']._serialized_end=1384
  _globals['_HIERARCHICALTABLEDESCRIPTOR']._serialized_start=1386
  _globals['_HIERARCHICALTABLEDESCRIPTOR']._serialized_end=1459
  _globals['_HIERARCHICALTABLEVIEWREQUEST']._serialized_start=1462
  _globals['_HIERARCHICALTABLEVIEWREQUEST']._serialized_end=1812
  _globals['_HIERARCHICALTABLEVIEWKEYTABLEDESCRIPTOR']._serialized_start=1815
  _globals['_HIERARCHICALTABLEVIEWKEYTABLEDESCRIPTOR']._serialized_end=1987
  _globals['_HIERARCHICALTABLEVIEWRESPONSE']._serialized_start=1989
  _globals['_HIERARCHICALTABLEVIEWRESPONSE']._serialized_end=2020
  _globals['_HIERARCHICALTABLESOURCEEXPORTREQUEST']._serialized_start=2023
  _globals['_HIERARCHICALTABLESOURCEEXPORTREQUEST']._serialized_end=2203
  _globals['_HIERARCHICALTABLESERVICE']._serialized_start=2281
  _globals['_HIERARCHICALTABLESERVICE']._serialized_end=2962
# @@protoc_insertion_point(module_scope)
