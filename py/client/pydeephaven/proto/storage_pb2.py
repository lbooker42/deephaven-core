# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: deephaven/proto/storage.proto
"""Generated protocol buffer code."""
from google.protobuf.internal import builder as _builder
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x1d\x64\x65\x65phaven/proto/storage.proto\x12!io.deephaven.proto.backplane.grpc\"J\n\x10ListItemsRequest\x12\x0c\n\x04path\x18\x01 \x01(\t\x12\x18\n\x0b\x66ilter_glob\x18\x04 \x01(\tH\x00\x88\x01\x01\x42\x0e\n\x0c_filter_glob\"\x81\x01\n\x08ItemInfo\x12\x0c\n\x04path\x18\x01 \x01(\t\x12\x39\n\x04type\x18\x02 \x01(\x0e\x32+.io.deephaven.proto.backplane.grpc.ItemType\x12\x10\n\x04size\x18\x03 \x01(\x12\x42\x02\x30\x01\x12\x11\n\x04\x65tag\x18\x04 \x01(\tH\x00\x88\x01\x01\x42\x07\n\x05_etag\"O\n\x11ListItemsResponse\x12:\n\x05items\x18\x01 \x03(\x0b\x32+.io.deephaven.proto.backplane.grpc.ItemInfo\"<\n\x10\x46\x65tchFileRequest\x12\x0c\n\x04path\x18\x01 \x01(\t\x12\x11\n\x04\x65tag\x18\x02 \x01(\tH\x00\x88\x01\x01\x42\x07\n\x05_etag\"A\n\x11\x46\x65tchFileResponse\x12\x10\n\x08\x63ontents\x18\x01 \x01(\x0c\x12\x11\n\x04\x65tag\x18\x02 \x01(\tH\x00\x88\x01\x01\x42\x07\n\x05_etag\"J\n\x0fSaveFileRequest\x12\x17\n\x0f\x61llow_overwrite\x18\x01 \x01(\x08\x12\x0c\n\x04path\x18\x02 \x01(\t\x12\x10\n\x08\x63ontents\x18\x03 \x01(\x0c\".\n\x10SaveFileResponse\x12\x11\n\x04\x65tag\x18\x01 \x01(\tH\x00\x88\x01\x01\x42\x07\n\x05_etag\"N\n\x0fMoveItemRequest\x12\x10\n\x08old_path\x18\x01 \x01(\t\x12\x10\n\x08new_path\x18\x02 \x01(\t\x12\x17\n\x0f\x61llow_overwrite\x18\x03 \x01(\x08\"\x12\n\x10MoveItemResponse\"&\n\x16\x43reateDirectoryRequest\x12\x0c\n\x04path\x18\x01 \x01(\t\"\x19\n\x17\x43reateDirectoryResponse\"!\n\x11\x44\x65leteItemRequest\x12\x0c\n\x04path\x18\x01 \x01(\t\"\x14\n\x12\x44\x65leteItemResponse*0\n\x08ItemType\x12\x0b\n\x07UNKNOWN\x10\x00\x12\r\n\tDIRECTORY\x10\x01\x12\x08\n\x04\x46ILE\x10\x02\x32\xfc\x05\n\x0eStorageService\x12x\n\tListItems\x12\x33.io.deephaven.proto.backplane.grpc.ListItemsRequest\x1a\x34.io.deephaven.proto.backplane.grpc.ListItemsResponse\"\x00\x12x\n\tFetchFile\x12\x33.io.deephaven.proto.backplane.grpc.FetchFileRequest\x1a\x34.io.deephaven.proto.backplane.grpc.FetchFileResponse\"\x00\x12u\n\x08SaveFile\x12\x32.io.deephaven.proto.backplane.grpc.SaveFileRequest\x1a\x33.io.deephaven.proto.backplane.grpc.SaveFileResponse\"\x00\x12u\n\x08MoveItem\x12\x32.io.deephaven.proto.backplane.grpc.MoveItemRequest\x1a\x33.io.deephaven.proto.backplane.grpc.MoveItemResponse\"\x00\x12\x8a\x01\n\x0f\x43reateDirectory\x12\x39.io.deephaven.proto.backplane.grpc.CreateDirectoryRequest\x1a:.io.deephaven.proto.backplane.grpc.CreateDirectoryResponse\"\x00\x12{\n\nDeleteItem\x12\x34.io.deephaven.proto.backplane.grpc.DeleteItemRequest\x1a\x35.io.deephaven.proto.backplane.grpc.DeleteItemResponse\"\x00\x42\x43H\x01P\x01Z=github.com/deephaven/deephaven-core/go/internal/proto/storageb\x06proto3')

_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, globals())
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'deephaven.proto.storage_pb2', globals())
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'H\001P\001Z=github.com/deephaven/deephaven-core/go/internal/proto/storage'
  _ITEMINFO.fields_by_name['size']._options = None
  _ITEMINFO.fields_by_name['size']._serialized_options = b'0\001'
  _ITEMTYPE._serialized_start=834
  _ITEMTYPE._serialized_end=882
  _LISTITEMSREQUEST._serialized_start=68
  _LISTITEMSREQUEST._serialized_end=142
  _ITEMINFO._serialized_start=145
  _ITEMINFO._serialized_end=274
  _LISTITEMSRESPONSE._serialized_start=276
  _LISTITEMSRESPONSE._serialized_end=355
  _FETCHFILEREQUEST._serialized_start=357
  _FETCHFILEREQUEST._serialized_end=417
  _FETCHFILERESPONSE._serialized_start=419
  _FETCHFILERESPONSE._serialized_end=484
  _SAVEFILEREQUEST._serialized_start=486
  _SAVEFILEREQUEST._serialized_end=560
  _SAVEFILERESPONSE._serialized_start=562
  _SAVEFILERESPONSE._serialized_end=608
  _MOVEITEMREQUEST._serialized_start=610
  _MOVEITEMREQUEST._serialized_end=688
  _MOVEITEMRESPONSE._serialized_start=690
  _MOVEITEMRESPONSE._serialized_end=708
  _CREATEDIRECTORYREQUEST._serialized_start=710
  _CREATEDIRECTORYREQUEST._serialized_end=748
  _CREATEDIRECTORYRESPONSE._serialized_start=750
  _CREATEDIRECTORYRESPONSE._serialized_end=775
  _DELETEITEMREQUEST._serialized_start=777
  _DELETEITEMREQUEST._serialized_end=810
  _DELETEITEMRESPONSE._serialized_start=812
  _DELETEITEMRESPONSE._serialized_end=832
  _STORAGESERVICE._serialized_start=885
  _STORAGESERVICE._serialized_end=1649
# @@protoc_insertion_point(module_scope)
