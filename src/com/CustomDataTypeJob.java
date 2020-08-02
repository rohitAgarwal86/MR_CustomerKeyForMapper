package com;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CustomDataTypeJob	{	

	//MAPPER
	//input key = 13152432 value = 0001,01-20-2013,400100,100,Exercise & Fitness,clarksville,credit
	//out key = 400100 value = 100,p1,d1
	public static class TxnMapper extends Mapper<LongWritable, Text, IntWritable, Transaction>{
		IntWritable outkey = new IntWritable();
		Transaction outvalue = new Transaction();
		
		public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
			String [] columns = value.toString().split(",");
			outkey.set(Integer.parseInt(columns[1]));
			outvalue.setAmount(Integer.parseInt(columns[2]));
			outvalue.setProduct(columns[3]);
			outvalue.setTxnDate(columns[0]);
			context.write(outkey, outvalue);
		}
		
	}
	
	//REDUCER
	public static class TxnReducer extends Reducer<IntWritable, Transaction,IntWritable, Text>{
		Text outvalue = new Text();
		public void reduce(IntWritable key,Iterable<Transaction> values,Context context) throws IOException, InterruptedException {
			Transaction max = new Transaction();
			for(Transaction value : values){
				if(value.getAmount() > max.getAmount()){
					max.set(value);
				}
			}
			outvalue.set("Max amount = "+max.getAmount()+" & product  = "+max.getProduct()+" & date = "+max.getTxnDate());
			context.write(key, outvalue);
		}
	}
	
	//DRIVER
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf,"My Cust Data Type class");
		job.setJarByClass(CustomDataTypeJob.class);
		job.setMapperClass(TxnMapper.class);
		job.setReducerClass(TxnReducer.class);
		
		//job.setNumReduceTasks(0);
			
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Transaction.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
		
	}

}