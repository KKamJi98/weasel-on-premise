resource "aws_s3_bucket" "example" {
  bucket = var.bucket_name

  tags = var.tags
}

resource "aws_s3_bucket_public_access_block" "example" {
  bucket = aws_s3_bucket.example.id

  block_public_acls       = var.public_access_block_enabled
  block_public_policy     = var.public_access_block_enabled
  ignore_public_acls      = var.public_access_block_enabled
  restrict_public_buckets = var.public_access_block_enabled
}

resource "aws_s3_bucket_policy" "example" {
  count = var.bucket_policy != "" ? 1 : 0

  bucket = aws_s3_bucket.example.id
  policy = var.bucket_policy

  depends_on = [
    aws_s3_bucket_public_access_block.example,
    aws_s3_bucket_acl.example
  ]
}

resource "aws_s3_bucket_website_configuration" "example" {
  count = var.enable_website ? 1 : 0

  bucket = aws_s3_bucket.example.id

  index_document {
    suffix = var.index_document
  }

  error_document {
    key = var.error_document
  }
}

resource "aws_s3_bucket_ownership_controls" "example" {
  count = var.public_access_block_enabled ? 0 : 1

  bucket = aws_s3_bucket.example.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

resource "aws_s3_bucket_acl" "example" {
  count = var.public_access_block_enabled ? 0 : 1

  depends_on = [
    aws_s3_bucket_ownership_controls.example,
    aws_s3_bucket_public_access_block.example,
  ]

  bucket = aws_s3_bucket.example.id
  acl    = "public-read"
}