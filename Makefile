VERILOG = LedGlow.v

prog : bin/toplevel.bin
	tinyprog -p bin/toplevel.bin

bin/toplevel.json : ${VERILOG}
	mkdir -p bin
	yosys -v3 -p "synth_ice40 -json bin/toplevel.json" ${VERILOG}

bin/toplevel.asc : tinyfpgabx.pcf bin/toplevel.json
	nextpnr-ice40 --freq 50 --lp8k --package cm81 --json bin/toplevel.json --pcf tinyfpgabx.pcf --asc bin/toplevel.asc --opt-timing

bin/toplevel.bin : bin/toplevel.asc
	icepack bin/toplevel.asc bin/toplevel.bin

compile : bin/toplevel.bin

time: bin/toplevel.bin
	icetime -tmd lp8k bin/toplevel.asc

clean :
	rm -rf bin
