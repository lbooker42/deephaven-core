FROM deephaven/protoc-base:local-build

COPY src/main/proto /includes

COPY dependencies /dependencies

RUN set -eux; \
  mkdir -p /generated/java; \
  mkdir -p /generated/grpc; \
  mkdir -p /generated/js; \
  mkdir -p /generated/go; \
  mkdir -p /generated/python; \
  mkdir -p /generated/cpp; \
  /opt/protoc/bin/protoc \
    --plugin=protoc-gen-grpc=/opt/java/bin/protoc-gen-grpc-java \
    --java_out=/generated/java \
    --grpc_out=/generated/grpc \
    -I/dependencies \
    -I/includes \
    /dependencies/BrowserFlight.proto \
    /includes/deephaven/proto/ticket.proto \
    /includes/deephaven/proto/console.proto \
    /includes/deephaven/proto/object.proto \
    /includes/deephaven/proto/session.proto \
    /includes/deephaven/proto/table.proto \
    /includes/deephaven/proto/application.proto \
    /includes/deephaven/proto/inputtable.proto \
    /includes/deephaven/proto/partitionedtable.proto \
    /includes/deephaven/proto/config.proto \
    /includes/deephaven/proto/hierarchicaltable.proto \
    /includes/deephaven/proto/storage.proto; \
  /opt/protoc/bin/protoc \
    --plugin=protoc-gen-ts=/usr/src/app/node_modules/.bin/protoc-gen-ts \
    --js_out=import_style=commonjs_strict:/generated/js \
    --ts_out=service=grpc-web:/generated/js \
    -I/dependencies \
    -I/includes \
    /dependencies/BrowserFlight.proto \
    /dependencies/Flight.proto \
    /includes/deephaven/proto/ticket.proto \
    /includes/deephaven/proto/console.proto \
    /includes/deephaven/proto/object.proto \
    /includes/deephaven/proto/session.proto \
    /includes/deephaven/proto/table.proto \
    /includes/deephaven/proto/application.proto \
    /includes/deephaven/proto/inputtable.proto \
    /includes/deephaven/proto/partitionedtable.proto \
    /includes/deephaven/proto/config.proto \
    /includes/deephaven/proto/hierarchicaltable.proto \
    /includes/deephaven/proto/storage.proto; \
  python3 -m grpc_tools.protoc \
    --grpc_python_out=/generated/python \
    --python_out=/generated/python \
    -I/includes \
    /includes/deephaven/proto/ticket.proto \
    /includes/deephaven/proto/console.proto \
    /includes/deephaven/proto/object.proto \
    /includes/deephaven/proto/session.proto \
    /includes/deephaven/proto/table.proto \
    /includes/deephaven/proto/application.proto \
    /includes/deephaven/proto/inputtable.proto \
    /includes/deephaven/proto/partitionedtable.proto \
    /includes/deephaven/proto/config.proto \
    /includes/deephaven/proto/hierarchicaltable.proto \
    /includes/deephaven/proto/storage.proto; \
  /opt/protoc/bin/protoc \
    --plugin=protoc-gen-go=/opt/go/bin/protoc-gen-go \
    --plugin=protoc-gen-go-grpc=/opt/go/bin/protoc-gen-go-grpc \
    --plugin=protoc-gen-cpp_grpc=/opt/deephaven/bin/grpc_cpp_plugin \
    --go_out=/generated/go \
    --go-grpc_out=/generated/go \
    --go_opt=module=github.com/deephaven/deephaven-core/go \
    --go-grpc_opt=module=github.com/deephaven/deephaven-core/go \
    --cpp_out=generated/cpp \
    --cpp_grpc_out=generated/cpp \
    -I/includes \
    /includes/deephaven/proto/ticket.proto \
    /includes/deephaven/proto/console.proto \
    /includes/deephaven/proto/object.proto \
    /includes/deephaven/proto/session.proto \
    /includes/deephaven/proto/table.proto \
    /includes/deephaven/proto/application.proto \
    /includes/deephaven/proto/inputtable.proto \
    /includes/deephaven/proto/partitionedtable.proto \
    /includes/deephaven/proto/config.proto \
    /includes/deephaven/proto/hierarchicaltable.proto \
    /includes/deephaven/proto/storage.proto;
