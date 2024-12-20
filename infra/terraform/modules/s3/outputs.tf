output "bucket_id" {
  description = "The ID of the S3 bucket"
  value       = aws_s3_bucket.example.id
}

output "bucket_arn" {
  description = "The ARN of the S3 bucket"
  value       = aws_s3_bucket.example.arn
}

output "website_endpoint" {
  description = "The website endpoint URL"
  value       = var.enable_website ? aws_s3_bucket_website_configuration.example[0].website_endpoint : ""
}