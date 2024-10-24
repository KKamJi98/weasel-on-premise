module "weasel_images_bucket" {
  source                      = "./modules/s3-bucket"
  bucket_name                 = "weasel-images"
  public_access_block_enabled = false
  # bucket_policy               = "" # 정책 없음
  bucket_policy = jsonencode({
    "Version" : "2008-10-17",
    "Id" : "PolicyForCloudFrontPrivateContent",
    "Statement" : [
      {
        "Effect": "Allow",
        "Principal": "*",
        "Action": "s3:GetObject",
        "Resource": "arn:aws:s3:::weasel-images/*",
        "Condition": {
          "StringLike": {
            "aws:Referer": [
              "https://weasel.kkamji.net/*"
            ]
          }
        }
      }
    ]
  })
  enable_website              = false
  tags = {
    Project = "weasel"
  }
}