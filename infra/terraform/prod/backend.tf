terraform {
  backend "s3" {
    bucket  = "weasel-terraform-state"
    key     = "persistent/terraform.tfstate"
    region  = "ap-northeast-2"
    encrypt = true
  }
}