package com;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Transaction implements Writable{
	
	private int amount = 0;
	private String product = null;
	private String txnDate = null;
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		amount = in.readInt();
		product = in.readUTF();
		txnDate = in.readUTF();
	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(amount);
		out.writeUTF(product);
		out.writeUTF(txnDate);
	}
	
	public void set(Transaction obj){
		this.amount = obj.amount;
		this.product = obj.product;
		this.txnDate = obj.txnDate;
	}
}