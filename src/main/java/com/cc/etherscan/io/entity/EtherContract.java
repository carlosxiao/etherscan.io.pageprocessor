package com.cc.etherscan.io.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author carlosxiao
 */

@TableName("ether_contract")
@Data
public class EtherContract extends Model<EtherContract> implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String address;

    @TableField("contract_name")
    private String contractName;

    @TableField("date_verified")
    private String dateVerified;

    @TableField("access_url")
    private String accessUrl;

    private Integer transactions;

    @TableField("create_address")
    private String createAddress;

    private String txn;

    @TableField("source_code")
    private String sourceCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
