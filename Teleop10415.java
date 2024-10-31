package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TeleOp10415")
public class Teleop10415 extends OpMode {
    
    DcMotor fr;
    DcMotor fl;
    DcMotor bl;
    DcMotor br;
    
    DcMotorEx leftArm;
    DcMotorEx rightArm;
    
    Servo shoulder;
    Servo wrist;
    Servo claw;
    
    DcMotor leftM;
    DcMotor rightM;
    
    public double shoulderOpen = 0.5;
    public double shoulderClose = 0.2;
    
    public double wristNormal=0.5;
    
    public double clawOpen=0.1;
    public double clawClose=0.225;

    @Override
    public void init() {
        fr = hardwareMap.dcMotor.get("bl");
        fl = hardwareMap.dcMotor.get("br");
        bl = hardwareMap.dcMotor.get("fr");
        br = hardwareMap.dcMotor.get("fl");
        
        shoulder = hardwareMap.servo.get("shoulder");
        wrist = hardwareMap.servo.get("wrist");
        claw = hardwareMap.servo.get("claw");
        
        leftM=hardwareMap.dcMotor.get("leftM");
        rightM=hardwareMap.dcMotor.get("rightM");
        
        leftArm=hardwareMap.get(DcMotorEx.class,"leftArm");
        rightArm=hardwareMap.get(DcMotorEx.class,"rightArm");
        
        shoulder.setPosition(shoulderOpen);
        wrist.setPosition(wristNormal);
        claw.setPosition(clawOpen);
        
        //rightArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //rightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightArm.setTargetPosition(0);
        leftArm.setTargetPosition(0);
        rightArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightArm.setPower(1);
        leftArm.setPower(1);
        leftM.setDirection(DcMotorSimple.Direction.REVERSE);
        rightArm.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        
        
    }
    
    public double speed = 0.85;
    public double startTime=0;
    public double startTime2=0;
    public boolean open=true;
    public boolean open2=true;
    public int arm=100;
    public int armChange=30;
    public int armMax=2200;
    
    @Override
    public void loop() {
        double y1 = gamepad1.left_stick_y;
        double x1 = gamepad1.left_stick_x;
        double x2 = 0.7*gamepad1.right_stick_x;
        
        double up = (gamepad2.right_trigger-gamepad2.left_trigger)+0.05;
        
        if (up>0&&leftArm.getCurrentPosition()<1900){
            if (leftM.getCurrentPosition()>2000)
                up=0;
            if (leftM.getCurrentPosition()>2100)
                up=-1;
        }
        
        leftM.setPower(up);
        rightM.setPower(up);
        
        if (gamepad2.dpad_up){
            arm=2150;
            leftArm.setTargetPosition(arm);
            rightArm.setTargetPosition(arm);
            leftArm.setVelocity(2000);
            rightArm.setVelocity(2000);
        }
        else if (gamepad2.dpad_down){
            arm=150;
            leftArm.setTargetPosition(arm);
            rightArm.setTargetPosition(arm);
            leftArm.setVelocity(500);
            rightArm.setVelocity(500);
        }
        else if (gamepad2.right_bumper&&arm<armMax){
            arm+=armChange;
            leftArm.setTargetPosition(arm);
            rightArm.setTargetPosition(arm);
            leftArm.setVelocity(2000);
            rightArm.setVelocity(2000);
        }
        else if (gamepad2.left_bumper&&arm>100){
            arm-=armChange;
            leftArm.setTargetPosition(arm);
            rightArm.setTargetPosition(arm);
            leftArm.setVelocity(500);
            rightArm.setVelocity(500);
        }
        
        if (gamepad2.y&&(System.currentTimeMillis()-startTime)>=300){
            startTime=System.currentTimeMillis();
            if (!open){
                shoulder.setPosition(shoulderOpen);
            }
            else{
                shoulder.setPosition(shoulderClose);
            }
            open=!open;
        }
        
        if (gamepad2.b){
            wrist.setPosition(wristNormal);
        }
        
        if (gamepad2.x&&(System.currentTimeMillis()-startTime2)>=300){
            startTime2=System.currentTimeMillis();
            if (open2){
                claw.setPosition(clawClose);
            }
            else{
                claw.setPosition(clawOpen);
                wrist.setPosition(0.5);
            }
            open2=!open2;
        }
        
        if (gamepad2.a){
            wrist.setPosition(0);
        }
        if (gamepad2.b){
            wrist.setPosition(0.5);
        }
        
        fr.setPower(speed*(y1+x1-x2));
        br.setPower(speed*(y1-x1-x2));
        fl.setPower(speed*(y1-x1+x2));
        bl.setPower(speed*(y1+x1+x2));
        
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        telemetry.addData("Speed", Math.round(speed*100));
        telemetry.addData("LeftArm",leftArm.getCurrentPosition());
        telemetry.addData("RightArm",rightArm.getCurrentPosition());
        telemetry.addData("LeftSlide",leftM.getCurrentPosition());
        telemetry.addData("RightSlide",rightM.getCurrentPosition());
        telemetry.update();
    }
}
