/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER. Copyright 1997-2015 NTG Clarity and/or its affiliates. All
 * rights reserved. NTG CLARITY is a leader in delivering network, telecom, IT and infrastructure solutions to network
 * service providers and medium and large enterprises. www.ntgclarity.com The contents of this file are subject to the
 * terms of "NTG Clarity License". You must not use this file except in compliance with the License. You can obtain a
 * copy of the License at http://www.ntgclarity.com/ See the License for the specific language governing permissions and
 * limitations under the License. Contributor(s): The Initial Developer of the Original Software is NTG Clarity . , Inc.
 * Copyright 1997-2012 NTG Clarity. All Rights Reserved. CLASS NAME <h4>Description</h4> <h4>Notes</h4>
 * <h4>References</h4>
 * @author: abadran <A HREF="mailto:[abadran@ntgclarity.com]">Alaa Baiomy</A>
 * @version Revision: 1.1.1.1 Date: March 18, 2019 
 * @see [String]
 * @see [URL]
 * @see [Class name#method name]
 */
package com.example.websocketdemo.exceptions;

public class BaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5904229413391409209L;

	StatusResponse status;

	public StatusResponse getStatus() {
		return status;
	}

	public BaseException(StatusResponse status) {
		this.status = status;
	}

	public BaseException(String code, String message) {
		super(message);
		this.status = new StatusResponse(code, message);

	}

	public BaseException(String code, String message, Throwable e) {
		super(message, e);
		this.status = new StatusResponse(code, message);

	}

	public BaseException(Throwable e) {
		super(e);
	}

}
