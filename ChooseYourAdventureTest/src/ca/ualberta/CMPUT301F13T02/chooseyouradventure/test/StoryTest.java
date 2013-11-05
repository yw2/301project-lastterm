/*
* Copyright (c) 2013, TeamCMPUT301F13T02
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without modification,
* are permitted provided that the following conditions are met:
* 
* Redistributions of source code must retain the above copyright notice, this
* list of conditions and the following disclaimer.
* 
* Redistributions in binary form must reproduce the above copyright notice, this
* list of conditions and the following disclaimer in the documentation and/or
* other materials provided with the distribution.
* 
* Neither the name of the {organization} nor the names of its
* contributors may be used to endorse or promote products derived from
* this software without specific prior written permission.
* 
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
* ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package ca.ualberta.CMPUT301F13T02.chooseyouradventure.test;

import java.util.UUID;

import android.test.InstrumentationTestCase;

import ca.ualberta.CMPUT301F13T02.chooseyouradventure.Page;
import ca.ualberta.CMPUT301F13T02.chooseyouradventure.Story;

public class StoryTest extends InstrumentationTestCase {

	public void testEquals() {

		UUID u1 = UUID.randomUUID();
		Page p1 = new Page();
		Story s1 = new Story();
		Story s2 = new Story();
		
		p1.setId(u1);

		s1.addPage(p1);
		s1.setFirstpage(u1);
		assertFalse(s1.equals(s2));
		
		s2.addPage(p1);
		s2.setFirstpage(u1);
		assertTrue(s1.equals(s2));
		
		//test title
		s1.setTitle("Ben's story");
		assertFalse(s1.equals(s2));
		
		s2.setTitle("Ben's story");
		assertTrue(s1.equals(s2));
		
		//test setting page
		s1.addPage(p1);
		assertFalse(s1.equals(s2));
		
		s2.addPage(p1);
		assertTrue(s1.equals(s2));
	}
}
