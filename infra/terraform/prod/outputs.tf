terraform {
  backend "s3" {
    bucket  = "terraform-state-weasel"
    key     = "persistent/terraform.tfstate"
    region  = "ap-northeast-2"
    encrypt = true
  }
}