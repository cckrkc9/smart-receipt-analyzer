# 🧾 Smart Receipt Analyzer

AI-powered receipt management system with cloud infrastructure and intelligent expense categorization.

## Features

- **JWT Authentication** - Secure user authentication and authorization
- **Receipt OCR** - Automatic text extraction with AWS Textract
- **Spring AI Integration** - LLM-powered expense classification with ChatClient
- **AI Categorization** - Smart expense classification using GPT-4
- **Expense Reports** - Automated category-based expense analysis
- **Cloud Storage** - Scalable receipt storage with AWS S3
- **NoSQL Database** - High-performance data management with DynamoDB
- **User Management** - Multi-user support with isolated data
- **RESTful API** - Clean and documented API endpoints

## Tech Stack

**Backend**
- Java 21
- Spring Boot 3.4.5
- Spring Security + JWT
- Spring AI 1.0.0 (OpenAI Integration)
- Maven

**Cloud & AI**
- AWS S3, DynamoDB, Textract
- Spring AI ChatClient with GPT-4
- OpenAI API

**Security**
- JWT Token Authentication
- BCrypt Password Encryption
- Stateless Session Management

### Environment Variables

- AWS_ACCESS_KEY_ID=your_key
- AWS_SECRET_ACCESS_KEY=your_secret
- AWS_REGION=eu-central-1
- AWS_S3_BUCKET=your-bucket
- OPENAI_API_KEY=your_key
- JWT_SECRET=your_secret

## Key Achievements

✅ Clean Architecture with layered design  
✅ Secure JWT-based authentication  
✅ Cloud-native AWS integration  
✅ Spring AI ChatClient for intelligent categorization  
✅ Asynchronous processing  
✅ RESTful API best practices  

## Roadmap

- [x] Core receipt processing system
- [x] JWT authentication
- [x] AWS services integration
- [x] Spring AI expense categorization
- [ ] Global exception handling
- [ ] DTO validation
- [ ] Unit & integration tests

