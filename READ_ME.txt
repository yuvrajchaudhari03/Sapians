Features:
   - JWT token.























admin create

I manually added auto increament for product table

Product Audit


ALTER TABLE `finalproject`.`vendor` 
CHANGE COLUMN `v_status` `v_status` BIT(1) NULL DEFAULT 0 ;

ALTER TABLE `finalproject`.`myorder` 
CHANGE COLUMN `o_id` `o_id` INT NOT NULL AUTO_INCREMENT ;

ALTER TABLE `finalproject`.`myorder` 
DROP COLUMN `qty`;



ngrok http -host-header=rewrite 3000

