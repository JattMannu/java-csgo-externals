package me.lixko.csgoexternals.util;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.structs.VectorMem;

public class LocalPlayerPosition {
	public VectorMem lpvec = new VectorMem();
	private MemoryBuffer lpvecbuf = new MemoryBuffer(lpvec.size());
	private float pos[] = new float[3], viewoffset[] = new float[3], vieworigin[] = new float[3];
	private float pitch, yaw, origyaw;
	public int fov, defaultfov;
	private float[] viewangles = new float[3], viewpunch = new float[3], aimpunch = new float[3];

	public LocalPlayerPosition() {
		lpvec.setSource(lpvecbuf);
	}

	public void updateData() {
		int lpobstarget = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Netvars.CBasePlayer.m_hObserverTarget) & Const.ENT_ENTRY_MASK;
		long lpaddr = 0;
		if(lpobstarget == Const.ENT_ENTRY_MASK) {
			lpaddr = Offsets.m_dwLocalPlayer;
		} else {
			lpaddr = MemoryUtils.getEntity(lpobstarget);
		}
		
		if(true) {
			Engine.clientModule().read(lpaddr + Netvars.CBaseEntity.m_vecOrigin, lpvec.size(), lpvecbuf);
			lpvec.copyTo(pos);
			
			Engine.clientModule().read(lpaddr + 0x3758, lpvec.size(), lpvecbuf);
			lpvec.copyTo(viewpunch);
			
			Engine.clientModule().read(lpaddr + 0x3764, lpvec.size(), lpvecbuf);
			lpvec.copyTo(aimpunch);
			
			Engine.clientModule().read(lpaddr + Offsets.m_vecViewOffset, lpvec.size(), lpvecbuf);
			viewoffset = lpvec.getVector();
			vieworigin = MathUtils.cadd(pos, viewoffset);
			
			Engine.clientModule().read(Offsets.m_dwClientState + Offsets.m_vecViewAngles, lpvecbuf.size(), lpvecbuf);
			origyaw = lpvec.y.getFloat();
			yaw = 90 - lpvec.y.getFloat();
			pitch = lpvec.x.getFloat();
			lpvec.copyTo(viewangles);
		} else {
			Engine.clientModule().read(Offsets.g_vecCurrentRenderOrigin, lpvec.size(), lpvecbuf);
			vieworigin = lpvec.getVector();
			pos = vieworigin;
			
			Engine.clientModule().read(Offsets.g_vecCurrentRenderAngles, lpvecbuf.size(), lpvecbuf);
			yaw = 90 - lpvec.y.getFloat();
			pitch = lpvec.x.getFloat();
		}

	}

	public float getX() {
		return this.pos[0];
	}

	public float getY() {
		return this.pos[1];
	}

	public float getZ() {
		return this.pos[2];
	}

	public int getFOV() {
		if (fov == 0)
			return defaultfov;
		return fov;
	}

	public float[] getViewOrigin() {
		return this.vieworigin;
	}

	public float[] getViewOffset() {
		return this.viewoffset;
	}

	public float[] getOrigin() {
		return this.pos;
	}

	public float getPitch() {
		return this.pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float[] getViewAngles() {
		return this.viewangles;
	}
	
	public float[] getViewPunch() {
		return this.viewpunch;
	}
	
	public float[] getAimPunch() {
		return this.aimpunch;
	}

}
