// Control logic for the processor

package CODCPU

import chisel3._
import chisel3.util.{BitPat, ListLookup}

/**
 * Main control logic for our simple processor
 *
 * Describe the I/O here
 *
 * For more information, see section 4.4 of Patterson and Hennessy
 * This follows figure 4.22
 */
class Control extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(7.W))

    val branch = Output(Bool())
    val memread = Output(Bool())
    val memtoreg = Output(Bool())
    val aluop = Output(UInt(2.W))
    val memwrite = Output(Bool())
    val regwrite = Output(Bool())
    val alusrc = Output(Bool())
  })

  val signals =
    ListLookup(io.opcode,
      /*default*/           List(false.B, false.B, false.B, 0.U,    false.B,  false.B, false.B),
      Array(                 /* branch,   memread, memtoreg, aluop, memwrite, alusrc,  regwrite */
      // R-format
      BitPat("b0110011") -> List(false.B, false.B, false.B, 2.U,    false.B,  false.B, true.B) ,
      // load
      BitPat("b0000011") -> List(false.B, true.B,  true.B,  0.U,    false.B,  true.B,  true.B) ,
      // store
      BitPat("b0100011") -> List(false.B, false.B, false.B, 0.U,    true.B,   true.B,  false.B),
      // beq
      BitPat("b1100011") -> List(true.B,  false.B, false.B, 1.U,    false.B,  false.B, false.B)
      ) // Array
    ) // ListLookup

  io.branch := signals(0)
  io.memread := signals(1)
  io.memtoreg := signals(2)
  io.aluop := signals(3)
  io.memwrite := signals(4)
  io.alusrc := signals(5)
  io.regwrite := signals(6)

}
