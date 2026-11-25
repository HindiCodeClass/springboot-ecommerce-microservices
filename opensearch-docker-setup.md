Production-Ready Single-Node OpenSearch Configuration in Docker
services:
  opensearch:
    image: opensearchproject/opensearch:2.15.0
    container_name: opensearch-prod
    environment:
      - cluster.name=opensearch-prod-cluster
      - node.name=opensearch-node1
      - discovery.type=single-node
      - bootstrap.memory_lock=true
      - "OPENSEARCH_JAVA_OPTS=-Xms4g -Xmx4g" # Kam se kam 4GB RAM
      - "OPENSEARCH_INITIAL_ADMIN_PASSWORD=${OPENSEARCH_PASSWORD}" # Strong password zaroori hai
    ulimits:
      memlock:
        soft: -1
        hard: -1
      nofile:
        soft: 65536
        hard: 65536
    ports:
      - "9200:9200"
    volumes:
      - opensearch_data:/usr/share/opensearch/data
      # Production mein apni custom SSL certificates bhi mount karni hoti hain
    restart: always

volumes:
  opensearch_data:


//////////////////////////
Local Development Setup for OpenSearch
services:
  opensearch:
    image: opensearchproject/opensearch:2.15.0
    container_name: opensearch-local
    environment:
      - discovery.type=single-node
      - "OPENSEARCH_JAVA_OPTS=-Xms512m -Xmx512m"
      - "DISABLE_INSTALL_DEMO_CONFIG=true"
      - "DISABLE_SECURITY_PLUGIN=true" # Sirf local dev ke liye
    ports:
      - "9200:9200"
    volumes:
      - opensearch_data:/usr/share/opensearch/data
    restart: unless-stopped

volumes:
  opensearch_data:
 