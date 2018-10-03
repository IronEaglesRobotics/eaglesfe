package eaglesfe.roverruckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import eaglesfe.common.MecanumDrive;

public class IronEaglesRobotRoverRuckus {

    private HardwareMap _hardwareMap;
    private MecanumDrive _drive;

    public IronEaglesRobotRoverRuckus(HardwareMap hardwareMap){
        this._hardwareMap = hardwareMap;
        initializeHardware();
    }

    public MecanumDrive get_drive() {
        return _drive;
    }

    private void initializeHardware(){
        // Initialize Drive Base
        DcMotor fl = this._hardwareMap.get(DcMotor.class, Constants.FrontLeft_Name);
        DcMotor fr = this._hardwareMap.get(DcMotor.class, Constants.FrontRight_Name);
        DcMotor bl = this._hardwareMap.get(DcMotor.class, Constants.BackLeft_Name);
        DcMotor br = this._hardwareMap.get(DcMotor.class, Constants.BackRight_Name);

        fl.setDirection(FORWARD);
        fr.setDirection(FORWARD);
        bl.setDirection(REVERSE);
        br.setDirection(REVERSE);

        _drive = new MecanumDrive(fl, fr, bl, br);
    }

    public class Constants {
        static final String FrontLeft_Name = "FrontLeft";
        static final String FrontRight_Name = "FrontRight";
        static final String BackLeft_Name = "BackLeft";
        static final String BackRight_Name = "BackRight";
    }
}