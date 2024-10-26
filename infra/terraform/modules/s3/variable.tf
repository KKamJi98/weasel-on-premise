variable "bucket_name" {
  description = "The name of the S3 bucket"
  type        = string
}

variable "public_access_block_enabled" {
  description = "Enable public access block for the S3 bucket"
  type        = bool
  default     = false
}

variable "tags" {
  description = "A map of tags to assign to the resource"
  type        = map(string)
  default     = {}
}

variable "bucket_policy" {
  description = "The bucket policy JSON"
  type        = string
  default     = ""
}

variable "index_document" {
  description = "The index document for the website"
  type        = string
  default     = "index.html"
}

variable "error_document" {
  description = "The error document for the website"
  type        = string
  default     = "error.html"
}

variable "enable_website" {
  description = "Enable static website hosting"
  type        = bool
  default     = false
}