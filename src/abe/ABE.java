/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package abe;
import co.junwei.cpabe.Cpabe;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class ABE {
	final static boolean DEBUG = true;
        static String pubfile = "pub_key.txt";
	static String mskfile = "master_key.txt";
	static String prvfile = "prv_key.txt";
        static String attr_str;
//	static String inputfile = "C:\\Users\\gowth\\OneDrive\\Documents\\NetBeansProjects\\ABE\\input.txt";
	static String encfile = "encrypted.enc";
	static String decfile = "output.txt";
	static String policy;
/*	static String student_attr = "objectClass:inetOrgPerson objectClass:organizationalPerson "
			+ "sn:student2 cn:student2 uid:student2 userPassword:student2 "
			+ "ou:idp o:computer mail:student2@sdu.edu.cn title:student course:maths year:2020";*/
	static String student_policy = "age:18 stdnt_major:Mathematics unique_quality:WildlifeConservationist school:GP 4of6";
	public static void main(String[] args) throws Exception {
            Scanner scanner = new Scanner(System.in);
//            String attr_str = student_attr;
            Cpabe test = new Cpabe();
            while(true){
                System.out.print("Enter Username: ");
                String username = scanner.nextLine();
                System.out.print("Enter Password: ");
                String pwd = scanner.nextLine();
                if(!username.equals("user") || !pwd.equals("user")){
                    System.out.println("Incorrect Password");
                }
                else{
                    while (true) {
                        System.out.println("1. Upload File");
                        System.out.println("2. Access File");
                        System.out.println("3. Exit");
                        System.out.println("Enter your choice:");
                        int choice = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice){
                            case 1:
                                System.out.print("Enter the filename: ");
                                String input = scanner.nextLine();
                              
                                File InputFile = new File(input);
                                if (InputFile.exists()) {
                                    System.out.print("Enter Age: ");
                                    String p_sn = scanner.nextLine();
                                    System.out.print("Enter Student Major: ");
                                    String p_uid = scanner.nextLine();
                                    System.out.print("Enter Unique Quality: ");
                                    String p_cn = scanner.nextLine();
                                    System.out.print("Enter School Name: ");
                                    String p_mail = scanner.nextLine();
                                    System.out.print("Enter number of failures: ");
                                    String c_fail = scanner.nextLine();
                                    System.out.print("Enter School Support: ");
                                    String s_support = scanner.nextLine();
                                    System.out.print("Enter Treshold Value n: ");
                                    int n = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Enter Treshold Value k: ");
                                    int k = scanner.nextInt();
                                    scanner.nextLine();
                                    policy = "age:"+p_sn+" stdnt_major:"+p_uid+" unique_quality:"+p_cn+" "+" school_name:"+p_mail+" class_failures:"+c_fail+" school_support:"+s_support+k+"of"+n;
                                    System.out.println(policy);
                                    println("//start to setup");
                                    test.setup(pubfile, mskfile);
                                    println("//end to setup");
                                    println("//start to enc");
                                    long begin_enc = System.currentTimeMillis();
                                    test.enc(pubfile, policy, input, encfile);
                                    long end_enc = System.currentTimeMillis();
                                    println("//end to enc");
                                    long time = end_enc-begin_enc;
                                    System.out.println();
                                    System.out.println("Encryption Time: "+time +" milli seconds");
                                }
                                else {
                                    System.out.println("File does not exist.");
                                }
                                break;
                            case 2:
                                    System.out.print("Enter Age: ");
                                    String a_sn = scanner.nextLine();
                                    System.out.print("Enter Student Major: ");
                                    String a_uid = scanner.nextLine();
                                    System.out.print("Enter Unique Quality: ");
                                    String a_cn = scanner.nextLine();
                                    System.out.print("Enter School Name: ");
                                    String a_mail = scanner.nextLine();
                                    System.out.print("Enter number of failures: ");
                                    String a_fail = scanner.nextLine();
                                    System.out.print("Enter School Support: ");
                                    String a_support = scanner.nextLine();
                                    attr_str = "age:"+a_sn+" stdnt_major:"+a_uid+" unique_quality:"+a_cn+" "+" school_name:"+a_mail+" class_failures:"+a_fail+" school_support:"+a_support;
                                println("//start to keygen");
                                long begin_key = System.currentTimeMillis();
                                test.keygen(pubfile, prvfile, mskfile, attr_str);
                                long end_key = System.currentTimeMillis();
                                long time_key = end_key-begin_key;
                                System.out.println();
                                System.out.println("Key Generation Time: "+time_key +" milli seconds");
                                println("//end to keygen");
                                System.out.print("Enter the filename: ");
                                encfile = scanner.nextLine();
                                File OutputFile = new File(encfile);
                                if (OutputFile.exists()) {
                                    println("//start to dec");
                                    long begin_dec = System.currentTimeMillis();
                                    test.dec(pubfile, prvfile, encfile, decfile);
                                    long end_dec = System.currentTimeMillis();
                                     long time = end_dec-begin_dec;
                                    System.out.println();
                                    System.out.println("Decryption Time: "+time +" milli seconds");
                                    println("//end to dec");
                                } 
                                else {
                                    System.out.println("File does not exist.");
                                }
                                
                                break;
                                
                            default:
                                System.exit(0);
                        }
                    }
                }
            }
	}
	/* connect element of array with blank */
	public static String array2Str(String[] arr) {
		int len = arr.length;
		String str = arr[0];
		for (int i = 1; i < len; i++) {
			str += " ";
			str += arr[i];
		}
		return str;
	}
	private static void println(Object o) {
		if (DEBUG)
			System.out.println(o);
	}
}
