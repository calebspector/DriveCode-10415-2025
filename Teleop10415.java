package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
    
    DcMotor leftArm;
    DcMotor rightArm;
    
    Servo shoulder;
    Servo wrist;
    Servo claw;
    Servo[] right;
    Servo[] left;
    
    DcMotor leftM;
    DcMotor rightM;
    
    public double shoulderOpen = 0.45;
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
        
        leftArm=hardwareMap.dcMotor.get("leftArm");
        rightArm=hardwareMap.dcMotor.get("rightArm");
        
        shoulder.setPosition(0);
        wrist.setPosition(wristNormal);
        claw.setPosition(clawOpen);
        
        //rightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
    
    @Override
    public void loop() {
        double y1 = gamepad1.left_stick_y;
        double x1 = gamepad1.left_stick_x;
        double x2 = 0.7*gamepad1.right_stick_x;
        
        double up = (gamepad2.right_trigger-gamepad2.left_trigger)+0.05;
        double up2 = gamepad1.right_trigger-gamepad1.left_trigger;
        
        if (true){//set condition with motor encoders for slides
            double thing =0;
            up2+=thing*(1-gamepad1.left_trigger);
        }
        
        if (gamepad2.right_bumper){
            up2=0.5;
        }
        else if (gamepad2.left_bumper){
            up2=-0.4;
        }
        
        leftM.setPower(up);
        rightM.setPower(up);
        
        leftArm.setPower(up2);
        rightArm.setPower(up2);
        
        if (gamepad2.y&&(System.currentTimeMillis()-startTime)>=300){
            startTime=System.currentTimeMillis();
            if (open){
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
        telemetry.update();
    }
}
