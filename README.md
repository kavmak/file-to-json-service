# File to JSON Service

A lightweight Spring Boot service that converts CSV, Excel, and Text files to JSON format through REST APIs.

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher
- Maven 3.6+

### Running the Service
```bash
cd file-to-json-service
mvn spring-boot:run
```

Service will start on **http://localhost:8084**

## 📋 API Endpoints

### 1. Upload File
**POST** `/api/files/upload`

Upload a file and get a unique file ID for later retrieval.

**Request:**
- Content-Type: `multipart/form-data`
- Parameter: `file` (the file to upload)

**Response:**
```json
{
  "success": true,
  "message": "File processed successfully",
  "fileId": "uuid-string",
  "recordCount": 150
}
```

**Example:**
```bash
curl -X POST -F "file=@data.csv" http://localhost:8084/api/files/upload
```

### 2. Get JSON Data
**GET** `/api/files/data/{fileId}`

Retrieve the JSON data for a previously uploaded file.

**Response:**
```json
{
  "success": true,
  "message": "Data retrieved successfully",
  "fileId": "uuid-string",
  "fileName": "data.csv",
  "fileType": "csv",
  "recordCount": 150,
  "data": [
    {
      "name": "John Doe",
      "age": "30",
      "email": "john@example.com"
    },
    {
      "name": "Jane Smith",
      "age": "25",
      "email": "jane@example.com"
    }
  ]
}
```

**Example:**
```bash
curl http://localhost:8084/api/files/data/your-file-id-here
```

### 3. List All Files
**GET** `/api/files/list`

Get a list of all uploaded file IDs.

**Response:**
```json
["file-id-1", "file-id-2", "file-id-3"]
```

### 4. Delete File
**DELETE** `/api/files/{fileId}`

Delete a file from memory.

**Response:**
```
File deleted successfully
```

## 📊 Supported File Formats

| Format | Extensions | Description |
|--------|------------|-------------|
| CSV | `.csv` | Comma-separated values |
| Excel | `.xls`, `.xlsx` | Microsoft Excel files |
| Text | `.txt` | Auto-detects delimiter (comma, pipe, tab, semicolon) |
| Pipe | `.psv` | Pipe-delimited files |

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
# Change port
server.port=8084

# File size limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## 💡 Usage Examples

### Java Integration
```java
// Upload file
RestTemplate restTemplate = new RestTemplate();
MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
body.add("file", new FileSystemResource("data.csv"));

HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.MULTIPART_FORM_DATA);

HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
FileUploadResponse response = restTemplate.postForObject(
    "http://localhost:8084/api/files/upload", 
    requestEntity, 
    FileUploadResponse.class
);

// Get JSON data
JsonDataResponse jsonData = restTemplate.getForObject(
    "http://localhost:8084/api/files/data/" + response.getFileId(),
    JsonDataResponse.class
);
```

### Python Integration
```python
import requests

# Upload file
with open('data.csv', 'rb') as f:
    response = requests.post(
        'http://localhost:8084/api/files/upload',
        files={'file': f}
    )
    file_id = response.json()['fileId']

# Get JSON data
json_response = requests.get(f'http://localhost:8084/api/files/data/{file_id}')
data = json_response.json()['data']
```

### JavaScript Integration
```javascript
// Upload file
const formData = new FormData();
formData.append('file', fileInput.files[0]);

const uploadResponse = await fetch('http://localhost:8084/api/files/upload', {
    method: 'POST',
    body: formData
});
const uploadResult = await uploadResponse.json();

// Get JSON data
const dataResponse = await fetch(`http://localhost:8084/api/files/data/${uploadResult.fileId}`);
const jsonData = await dataResponse.json();
```

## 🔒 Features

- **In-Memory Storage**: Fast processing with no database dependencies
- **Multiple Formats**: Support for CSV, Excel, and various text formats
- **Auto-Detection**: Automatically detects delimiters in text files
- **RESTful API**: Simple HTTP endpoints for easy integration
- **CORS Enabled**: Ready for web application integration
- **Error Handling**: Comprehensive error responses
- **File Management**: Upload, retrieve, list, and delete operations

## 🏗️ Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client App    │───▶│  File Controller │───▶│ Processing      │
│                 │    │                  │    │ Service         │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                                         │
                                                         ▼
                       ┌─────────────────────────────────────────┐
                       │            File Parsers                │
                       │  ┌─────────┐ ┌─────────┐ ┌─────────┐   │
                       │  │   CSV   │ │  Excel  │ │   TXT   │   │
                       │  │ Parser  │ │ Parser  │ │ Parser  │   │
                       │  └─────────┘ └─────────┘ └─────────┘   │
                       └─────────────────────────────────────────┘
```

## 📈 Performance

- **Memory Usage**: Files stored in memory for fast access
- **File Size Limit**: 10MB per file (configurable)
- **Concurrent Processing**: Thread-safe operations
- **Response Time**: Sub-second processing for typical files

## 🔄 Workflow

1. **Upload**: Client uploads file via POST `/api/files/upload`
2. **Process**: Service parses file and converts to JSON
3. **Store**: JSON data stored in memory with unique ID
4. **Retrieve**: Client fetches JSON data via GET `/api/files/data/{fileId}`
5. **Use**: Client processes JSON data and stores in their database

---

**Built for simple file-to-JSON conversion needs**