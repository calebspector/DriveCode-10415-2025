package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DistanceSensor;



@TeleOp(name = "TeleopIMUBlue10415")
public class TeleOpIMU10415 extends OpMode {
    
    DcMotor fr;
    DcMotor fl;
    DcMotor bl;
    DcMotor br;
    
    Servo grab;
    Servo frontRotate;
    Servo spinny;
    Servo split;
    Servo left;
    Servo right;
    Servo arm;
    DcMotor drone;
    
    DcMotor leftM;
    DcMotor rightM;
    
    DistanceSensor distance1;
    DistanceSensor distance3;
    DistanceSensor zalan;
    
    IMU imu;
    @Override
    public void init() {
        imu=hardwareMap.get(IMU.class,"imu");
        //Motors
        
        fr = hardwareMap.dcMotor.get("bl");
        fl = hardwareMap.dcMotor.get("br");
        bl = hardwareMap.dcMotor.get("fr");
        br = hardwareMap.dcMotor.get("fl");
        
        frontRotate = hardwareMap.servo.get("frontRotate");
        grab = hardwareMap.servo.get("grab");
        spinny = hardwareMap.servo.get("spinny");
        split=hardwareMap.servo.get("split");
        left=hardwareMap.servo.get("left");
        right=hardwareMap.servo.get("right");
        arm=hardwareMap.servo.get("arm");
        drone=hardwareMap.dcMotor.get("drone");
        
        leftM=hardwareMap.dcMotor.get("leftM");
        rightM=hardwareMap.dcMotor.get("rightM");
        
        distance1 = hardwareMap.get(DistanceSensor.class, "d2");
        distance3 = hardwareMap.get(DistanceSensor.class, "d4");
        zalan=hardwareMap.get(DistanceSensor.class,"cd");
        
        left.setPosition(0);
        right.setPosition(0);
        arm.setPosition(0.825);
        split.setPosition(0.45);
        spinny.setPosition(0.19);
        //spinny.setPosition(0.5);
        grab.setPosition(0.3);
        frontRotate.setPosition(0.96);
        //drone.setPosition(1);
        
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        rightM.setDirection(DcMotorSimple.Direction.REVERSE);
        rightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT,RevHubOrientationOnRobot.UsbFacingDirection.UP)));
    }
    
    public double speed = -0.85;

    public double startTime;
    public double startTime2;
    public double startTime3;
    public double startTime4;
    public int VERSION = 1;
    public boolean open = true;
    public boolean open2=true;
    public boolean open3=true;
    public boolean open4=true;
    public boolean open5=true;
    public boolean splits=true;
    public long timer=-1;
    public int num=1;
    public boolean movingL=false;
    public boolean movingR=false;
    @Override
    public void loop() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double angle = orientation.getYaw(AngleUnit.RADIANS);
        telemetry.addLine("VERSION:" + VERSION);
        double y1 = Math.cos(angle)*gamepad1.left_stick_y+Math.sin(angle)*gamepad1.left_stick_x+0.5*gamepad2.right_stick_y;
        double x1 = Math.cos(angle)*gamepad1.left_stick_x+Math.sin(angle)*-gamepad1.left_stick_y+0.5*gamepad2.left_stick_x;
        double x2 = 0.7*gamepad1.right_stick_x;
        double up = (gamepad2.right_trigger-gamepad2.left_trigger-0.5*gamepad2.left_stick_y)+0.05;
        double x22 = gamepad2.left_stick_y;
        double y2=0;
        telemetry.addData("Angle:",180*angle/Math.PI);
        telemetry.addData("pitch:",orientation.getPitch(AngleUnit.DEGREES));
        telemetry.addData("roll:",orientation.getRoll(AngleUnit.DEGREES));
        leftM.setPower(up);
        rightM.setPower(up);
        if (gamepad1.b){
            imu.resetYaw();
        }
        if (leftM.getCurrentPosition()<0){
            leftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (rightM.getCurrentPosition()<0){
            rightM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (movingL){
            //leftM.setPower((leftM.getTargetPosition()-leftM.getCurrentPosition())/15.0);
            if (leftM.getCurrentPosition()>50){
                leftM.setPower(-1);
            }
            else{
                leftM.setPower(1);
            }
        }
        if (movingR){
            //rightM.setPower((rightM.getTargetPosition()-rightM.getCurrentPosition())/15.0);
            if (rightM.getCurrentPosition()>50){
                rightM.setPower(-1);
            }
            else{
                rightM.setPower(1);
            }
        }
        // speed=1;
        if (Math.abs(leftM.getCurrentPosition()-leftM.getTargetPosition())<5){
            leftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            movingL=false;
        }
        if (Math.abs(rightM.getCurrentPosition()-rightM.getTargetPosition())<5){
            movingR=false;
            rightM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (timer!=-1&&num==1&&System.currentTimeMillis()-timer>1250){
            grab.setPosition(0.2);
            spinny.setPosition(0.8);
            timer=System.currentTimeMillis();
            num++;
        }
        
        if (timer!=-1&&num==2&&System.currentTimeMillis()-timer>500){
            spinny.setPosition(0.19);
            grab.setPosition(0);
            frontRotate.setPosition(0.96);
            split.setPosition(0.45/2);
            timer=System.currentTimeMillis();
            num++;
        }
        
        if (timer!=-1&&num==3&&System.currentTimeMillis()-timer>750){
            if (splits)
                split.setPosition(0);
            else{
                split.setPosition(0.45);
            }
            timer=System.currentTimeMillis();
            num++;
        }
        
        if (timer!=-1&&num==4&&System.currentTimeMillis()-timer>1000){
            if (splits)
                split.setPosition(0.45);
            else{
                split.setPosition(0);
            }
            timer=System.currentTimeMillis();
            num++;
        }
        
        if (timer!=-1&&num==5&&System.currentTimeMillis()-timer>750){
            if (splits)
                split.setPosition(0);
            else
                split.setPosition(0.45);
            splits=!splits;
            timer=System.currentTimeMillis();
            num++;
        }
        
        if (timer!=-1&&num==6){
            rightM.setTargetPosition(0);
            leftM.setTargetPosition(0);
            leftM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightM.setPower(-1);
            leftM.setPower(-1);
            if (leftM.getCurrentPosition()<25&&rightM.getCurrentPosition()<25){
                num++;
            }
            timer=System.currentTimeMillis();
        }
        if (timer!=-1&&num==7){
            grab.setPosition(0.3);
            left.setPosition(0.22);
            timer=-1;
            num++;
        }
        
        if (gamepad1.a){
            y1=-1*(distance1.getDistance(DistanceUnit.CM)-6)/25.0;
            y2=-1*(distance3.getDistance(DistanceUnit.CM)-6)/25.0;
        }
        if (gamepad2.y&&(System.currentTimeMillis()-startTime)>=300){
            startTime=System.currentTimeMillis();
            if (open4){
                left.setPosition(0.22);
            }
            else{
                left.setPosition(0);
            }
            open4=!open4;
        }
        if ((gamepad2.a) && ((System.currentTimeMillis()-startTime)>=300))
        {
            startTime = System.currentTimeMillis();
            // if(open&&open2!=0){
            //     //grab.setPosition(0.3);
            //     left.setPosition(0.22);
            //     right.setPosition(0.22);
            //     //arm.setPosition(0.5);
            // }
            if (open){
                grab.setPosition(0);
            }
            
            // else if (!open&&open2!=0){
            //     left.setPosition(0);
            //     right.setPosition(0);
            // }
            
            else {
                grab.setPosition(0.3);
            }
            
            open=!open;
        }
        if (gamepad2.b && ((System.currentTimeMillis()-startTime2)>=300)&&leftM.getCurrentPosition()>500&&rightM.getCurrentPosition()>500){
            startTime2 = System.currentTimeMillis();
            if (open5){
                arm.setPosition(0.5);
            }
            else{
                arm.setPosition(0.825);
            }
            open5=!open5;
        }
        if (gamepad2.x && ((System.currentTimeMillis()-startTime2)>=300)&&(!open))
        {
            startTime2 = System.currentTimeMillis();
            
            if(true){
                // spin.setPosition(0.945);
                // rotate.setPosition(0.055);
                // aa.setPosition(0.7);
                frontRotate.setPosition(0.225);
                spinny.setPosition(0.3);
                timer=(long)System.currentTimeMillis();
                num=1;
                open=true;
                leftM.setTargetPosition(50);
                rightM.setTargetPosition(50);
                leftM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                movingL=true;
                movingR=true;
                // if (rightM.getCurrentPosition()>50){
                //     leftM.setPower(-1);
                //     rightM.setPower(-1);
                // }
                // else{
                //     leftM.setPower(1);
                //     rightM.setPower(1);
                // }
            }  
            // else if (open2==1&&rightM.getCurrentPosition()>=500) {
            //     // spin.setPosition(0.03);
            //     // rotate.setPosition(0.97);
            //     // aa.setPosition(0.25);
            //     // frontRotate.setPosition(1);
            //     // spinny.setPosition(0.12);
            //     open=false;
            //     arm.setPosition(0.5);
            // }
            
            // else if (open2==2){
            //     arm.setPosition(0.825);
            // }
            // else{
            //     open2--;
            // }
            
            // open2++;
            // open2=open2%3;
            open2=!open2;
        }
        speed=-1;
        if(gamepad1.y){
            speed = -1;
        }
        
        if(gamepad1.x){
            speed=-0.5;
        }
        if (gamepad1.right_bumper&&gamepad1.left_bumper){
            drone.setPower(1);
        }
        else{
            drone.setPower(0);
        }
//         if (gamepad2.y && ((System.currentTimeMillis()-startTime3)>=300))
//         {
//             startTime3 = System.currentTimeMillis();
//             if(open3){
//                 gr.setPosition(0.545);
//                 gl.setPosition(0.47);
//             } else {
//                 gl.setPosition(0);
//                 gr.setPosition(1);
//             }
//             open3 = !open3;
//         }
        
//         // if (gamepad2.b){
//         //     spin.setPosition(0.7);
//         // }
//         // if (gamepad2.x){
//         //     spin.setPosition(0.05);
//         // }
//         // if (gamepad2.y){
//         //     spin.setPosition(0.375);
//         // }
//         if (gamepad1.left_trigger>0.1){
//             speed=0.3;
//         }
//         if (gamepad1.right_trigger>0.1){
//             speed=1;
//         }

        if (gamepad2.right_bumper && ((System.currentTimeMillis()-startTime3)>=300))
        {
            startTime3 = System.currentTimeMillis();
            spinny.setPosition(spinny.getPosition()+((.5-0.19)/5));
        }
        
        else if (gamepad2.left_bumper && ((System.currentTimeMillis()-startTime3)>=300))
        {
            startTime3 = System.currentTimeMillis();
            spinny.setPosition(spinny.getPosition()-((.5-0.19)/5));
        }
//         else{
//             llls.setPower(up);
//             ulls.setPower(up);
//             lrls.setPower(up);
//             urls.setPower(up);
//         }
        
        fr.setPower(speed*(y1+x1+x2));
        br.setPower(speed*(y1-x1+x2));
        fl.setPower(speed*(y1-x1-x2));
        bl.setPower(speed*(y1+x1-x2));
        if (y2!=0){
            fl.setPower(speed*(y2-x1-x2));
            bl.setPower(speed*(y2+x1-x2));
        }
        
        // llls.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // ulls.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // lrls.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // urls.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        //telemetry.addData("d1",distance1.getDistance(DistanceUnit.CM));
        //telemetry.addData("d3",distance3.getDistance(DistanceUnit.CM));
        telemetry.addData("Speed", speed);
        telemetry.addData("Speed", Math.round(speed*100));
        // telemetry.addData("Left",leftM.getCurrentPosition());
        // telemetry.addData("Right",rightM.getCurrentPosition());
        // telemetry.addData("Left",leftM.getTargetPosition());
        // telemetry.addData("Right",rightM.getTargetPosition());
        //telemetry.addData("Zalan",zalan.getDistance(DistanceUnit.CM));
        telemetry.addData("Open",open);
        telemetry.update();
        
    }
}
