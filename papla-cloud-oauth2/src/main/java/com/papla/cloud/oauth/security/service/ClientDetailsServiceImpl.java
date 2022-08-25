package com.papla.cloud.oauth.security.service;

import com.papla.cloud.oauth.security.enums.PasswordEncoderTypeEnum;
import com.papla.cloud.oauth.security.domain.OAuthClient;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;


@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Override
    @SneakyThrows
    public ClientDetails loadClientByClientId(String clientId) {
        try {
            OAuthClient client =new OAuthClient();
            BaseClientDetails clientDetails = new BaseClientDetails(
                    client.getClientId(),
                    client.getResourceIds(),
                    client.getScope(),
                    client.getAuthorizedGrantTypes(),
                    client.getAuthorities(),
                    client.getWebServerRedirectUri()
            );
            clientDetails.setClientSecret(PasswordEncoderTypeEnum.NOOP.getPrefix() + client.getClientSecret());
            clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
            clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
            return clientDetails;
        } catch (EmptyResultDataAccessException var4) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }
}
