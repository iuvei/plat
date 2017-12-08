package com.gameportal.pay.utils;

import com.gameportal.pay.model.fcs.request.FCSOpenApiRequest;
import com.gameportal.pay.model.fcs.response.FCSOpenApiResponse;

public abstract interface FCSOpenApiClient {
	public abstract FCSOpenApiResponse excute(FCSOpenApiRequest paramFCSOpenApiRequest, String paramString)
			throws Exception;
}
