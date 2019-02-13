/**
 *
 */
package com.broadsoft.ccone.rest.client.manager;

import java.util.Arrays;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.broadsoft.ccone.rest.client.helper.ApiPropertiesHelper;
import com.broadsoft.ccone.rest.client.pojo.HazelcastConfiguration;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.transerainc.tam.config.AccessDetails.HazelcastAccessDetails;

/**
 * @author svytla
 */
@Component
public class HazelcastManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HazelcastManager.class);

    @Autowired
    private ApiPropertiesHelper propsUtil;

    private static HazelcastAccessDetails accessDetails;

    private static HazelcastInstance instance;

    private static String nodeAddress;

    public void start(final HazelcastAccessDetails accessDetails) {
        HazelcastManager.accessDetails = accessDetails;
        shutdown();
        build();
    }

    public void shutdown() {
        if (instance != null) {
            LOGGER.info("Shutting down the client");
            instance.shutdown();
        }
    }

    public void build() {

        final String quorum = StringUtils.join(accessDetails.getCluster(), ",");

        LOGGER.info("Starting hazelcast client using quoram {}", quorum);

        final HazelcastConfiguration info = new HazelcastConfiguration();

        info.setName(StringUtils.trim(accessDetails.getGroupName()));
        info.setPassword(StringUtils.trim(accessDetails.getGroupPassword()));
        info.setMembers(quorum);
        info.setSmartRouting(BooleanUtils.toBoolean(propsUtil.getYmlProperty("hazelcast.network.smart.routing")));
        info.setRedoOperations(BooleanUtils.toBoolean(propsUtil.getYmlProperty("hazelcast.network.redo.operations")));
        info.setConnectionTimeout(NumberUtils.toInt(propsUtil.getYmlProperty("hazelcast.network.connection.timeout")));
        info.setConnectionAttemptLimit(
                NumberUtils.toInt(propsUtil.getYmlProperty("hazelcast.network.connection.attempt.limit")));
        info.setConnectionAttemptPeriod(
                NumberUtils.toInt(propsUtil.getYmlProperty("hazelcast.network.connection.attempt.period")));

        final String[] hosts = accessDetails.getCluster();

        // final List<String> addresses = new ArrayList<>();
        // if (hosts != null) {
        // for (final String host : hosts) {
        // addresses.add(StringUtils.trim(host));
        // }
        // }

        LOGGER.warn("Creating hazelcast client with configuration {}", info);

        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.setGroupConfig(new GroupConfig(info.getName(), info.getPassword()));

        final ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.addAddress(hosts);

        networkConfig.setSmartRouting(info.isSmartRouting());
        networkConfig.setRedoOperation(info.isRedoOperations());

        networkConfig.setConnectionTimeout(info.getConnectionTimeout());
        networkConfig.setConnectionAttemptLimit(info.getConnectionAttemptLimit());
        networkConfig.setConnectionAttemptPeriod(info.getConnectionAttemptPeriod());

        instance = HazelcastClient.newHazelcastClient(clientConfig);

        setAppHostName();

        LOGGER.warn("Created the hazelcast client with hosts {}", Arrays.toString(hosts));

    }

    private void setAppHostName() {
        nodeAddress = propsUtil.getYmlProperty("app.host.name");
    }

    /**
     * @return the client
     */
    public HazelcastInstance getInstance() {
        return instance;
    }

    /**
     * @return the nodeAddress
     */
    public static String getNodeAddress() {
        return nodeAddress;
    }
}
