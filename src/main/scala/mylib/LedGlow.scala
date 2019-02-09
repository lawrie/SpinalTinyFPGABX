package mylib

import spinal.core._

class LedGlow extends Component {
  val io = new Bundle {
    val led = out Bool
  }

  val width = 24

  val cnt = Reg(UInt(width bits))
  val pwm = Reg(UInt(5 bits))

  cnt := cnt + 1

  val pwmInput = UInt(4 bits)

  when (cnt(width - 1)) {
    pwmInput := cnt(width -2 downto width - 5)
  } otherwise {
    pwmInput := ~cnt(width - 2 downto width - 5)
  }

  pwm := pwm(3 downto 0).resize(5) + pwmInput.resize(5)

  io.led := pwm(4)  
}

//Define a custom SpinalHDL configuration with synchronous reset instead of the default asynchronous one. This configuration can be resued everywhere
object TinyFPGABXSpinalConfig extends SpinalConfig(defaultConfigForClockDomains = ClockDomainConfig(resetKind = BOOT))

object LedGlow {
  def main(args: Array[String]) {
    TinyFPGABXSpinalConfig.generateVerilog(new LedGlow)
  }
}

