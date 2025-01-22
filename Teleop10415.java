package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import java.util.*;


@TeleOp(name = "TeleOp10415")
public class Teleop10415 extends OpMode {
    
    DcMotor fr;
    DcMotor fl;
    DcMotor bl;
    DcMotor br;
    
    DcMotorEx rightArm;
    
    Servo shoulder;
    Servo wrist;
    Servo claw;
    
    DcMotor leftM;
    DcMotor rightM;
    DcMotor leftM2;
    
    Limelight3A limelight;
    
    public double shoulderOpen = 0.22;
    public double shoulderClose = 0.94;
    
    public double wristNormal=0.525;
    public double wrist90=0;
    
    public double clawOpen=0.7;
    public double clawClose=0.3;
    
    public int motorRPM = 435;
    public double mult=435.0/(double)motorRPM;

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
        leftM2=hardwareMap.dcMotor.get("leftM2");
        
        rightArm=hardwareMap.get(DcMotorEx.class,"rightArm");
        
        limelight = hardwareMap.get(Limelight3A.class,"limelight");
        telemetry.setMsTransmissionInterval(30);

        limelight.pipelineSwitch(0);

        limelight.start();
        
        
        //rightArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //rightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftM2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightM.setDirection(DcMotorSimple.Direction.REVERSE);
        leftM2.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        
        rightArm.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    
    public double speed = 0.85;
    public double startTime=0;
    public double startTime2=0;
    public double startTime3=0;
    public double close=0;
    public boolean open=true;
    public boolean open2=true;
    public int arm=0;
    public int armChange=30;
    public int armMax=2100;
    public boolean init=false;
    public boolean runToPosition=false;
    public boolean openShoulder=true;
    public double waitSec=0;
    public double startHang=0;
    @Override
    public void loop() {
        if (!init){
            //shoulder.setPosition(0.65);
            wrist.setPosition(wristNormal);
            claw.setPosition(clawOpen);
            init=true;
        }
        double y1 = gamepad1.left_stick_y;
        double x1 = gamepad1.left_stick_x;
        double x2 = 0.7*gamepad1.right_stick_x;
        double zalan = -1*gamepad2.left_stick_y;
        
        double up = (gamepad2.right_trigger-gamepad2.left_trigger)+0.05;
        
        LLResult result = limelight.getLatestResult();
        
        if (result.isValid()){
            List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
                    for (LLResultTypes.ColorResult cr : colorResults) {
                        telemetry.addData("Color", "X: %.2f, Y: %.2f", cr.getTargetXDegrees(), cr.getTargetYDegrees());
                    }
        }
        
    
        if (waitSec!=0 && System.currentTimeMillis()-waitSec>750){
            waitSec=0;
            shoulder.setPosition(shoulderClose);
            openShoulder=false;
        }
        if (close!=0 && System.currentTimeMillis()-close>750){
            close=0;
            openShoulder=true;
            shoulder.setPosition(0.65);
        }
        if (up>0&&rightArm.getCurrentPosition()<950){
            if (leftM.getCurrentPosition()>1300*mult)
                up=0;
            if (leftM.getCurrentPosition()>1400*mult)
                up=-1;
        }
        if (gamepad1.right_bumper&&gamepad1.left_bumper){
            if (startHang==0){
                runToPosition=true;
                arm=200;
                startHang=System.currentTimeMillis();
            }
            else if (System.currentTimeMillis()-startHang>600){
                leftM.setPower(-1);
                rightM.setPower(-1);
                leftM2.setPower(-1);
            }
        }
        else if (leftM.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)){
            if (Math.abs(up)>0.5||(Math.abs(rightM.getCurrentPosition()-rightM.getTargetPosition())<50&&Math.abs(leftM.getCurrentPosition()-leftM.getTargetPosition())<50)){
                leftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                leftM2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rightM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                leftM.setPower(up);
                rightM.setPower(up);
                leftM2.setPower(up);
            }
        }
        else{
            leftM.setPower(up);
            rightM.setPower(up);
            leftM2.setPower(up);
        }
        
        if (gamepad1.dpad_down){
            runToPosition=true;
            arm=750;
        }
        else if (gamepad2.dpad_right){
            shoulder.setPosition(0.04);
            open2=true;
            claw.setPosition(clawOpen);
            runToPosition=true;
            arm=1900;
            openShoulder=true;
            runSlidesTo(0);
        }
        else if (gamepad2.dpad_left){
            runToPosition=true;
            arm=1300;
            shoulder.setPosition(shoulderClose);
            openShoulder=false;
            runSlidesTo(900);
        }
        else if (gamepad1.dpad_up||gamepad2.dpad_up){
            runToPosition=true;
            arm=2000;
            rightArm.setPower(1);
            shoulder.setPosition(0.55);
            openShoulder=true;
            runSlidesTo(0);
            waitSec=System.currentTimeMillis();
        }
        else if ((gamepad2.dpad_down)){
            runToPosition=true;
            arm=50;
            rightArm.setPower(-1);
            runSlidesTo(0);
        }
        else if (zalan!=0&&(rightArm.getCurrentPosition()<armMax||zalan<0)){
            if (runToPosition&&Math.abs(zalan)>0.5){
                runToPosition=false;
            }
            rightArm.setPower(zalan);
        }
        else if (!runToPosition){
            rightArm.setPower(0);
        }
        if (runToPosition){
            if (Math.abs(rightArm.getCurrentPosition()-arm)<50){
                runToPosition=false;
            }
            else if (rightArm.getCurrentPosition()>arm){
                rightArm.setPower(-1);
            }
            else if (rightArm.getCurrentPosition()<arm){
                rightArm.setPower(1);
            }
        }
        double up2 = gamepad1.right_trigger-gamepad1.left_trigger;
        if (Math.abs(up2)>0.5){
            rightArm.setPower(-1*up2);
            runToPosition=false;
        }
        
        if (gamepad2.y&&(System.currentTimeMillis()-startTime)>=300){
            startTime=System.currentTimeMillis();
            if (openShoulder){
                shoulder.setPosition(shoulderClose);
            }
            else{
                shoulder.setPosition(shoulderOpen);
            }
            openShoulder=!openShoulder;
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
            }
            open2=!open2;
        }
        
        //if (gamepad2.a&&(System.currentTimeMillis()-startTime3)>=300){
        
        if (gamepad2.a&&(System.currentTimeMillis()-startTime3)>=300){
            startTime3=System.currentTimeMillis();
            if (openShoulder){
                shoulder.setPosition(shoulderClose);
                close = System.currentTimeMillis();
                open2=false;
                claw.setPosition(clawClose);
            }
            else{
                shoulder.setPosition(0.65);
            }
            openShoulder=!openShoulder;
        }
        /*
        if (gamepad2.b){
            wrist.setPosition(1);
        }
        */
        if (gamepad2.right_bumper){
            wrist.setPosition(wristNormal);
        }
        else if (gamepad2.left_bumper){
            wrist.setPosition(wrist90);
        }
        
        fr.setPower(speed*(y1+x1-x2));
        br.setPower(speed*(y1-x1-x2));
        fl.setPower(speed*(y1-x1+x2));
        bl.setPower(speed*(y1+x1+x2));
        
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        telemetry.addData("Speed", Math.round(speed*100));
        telemetry.addData("RightArm",rightArm.getCurrentPosition());
        telemetry.addData("Arm",arm);
        telemetry.addData("LeftSlide",leftM.getCurrentPosition());
        telemetry.addData("LeftSlide2",leftM2.getCurrentPosition());
        telemetry.addData("RightSlide",rightM.getCurrentPosition());
        telemetry.addData("SlideMode",leftM.getMode());
        telemetry.update();
    }
    public void runSlidesTo(int val){
        rightM.setTargetPosition(val);
        leftM.setTargetPosition(val);
        leftM2.setTargetPosition(val);
        rightM.setPower(1);
        leftM.setPower(1);
        leftM2.setPower(1);
        rightM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftM2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
