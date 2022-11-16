package com.songoda.core.configuration.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.events.CommentEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public class YamlCommentRepresenter extends Representer {
    private final Map<String, Supplier<String>> nodeComments;

    public YamlCommentRepresenter(DumperOptions dumperOptions, Map<String, Supplier<String>> nodeComments) {
        super(dumperOptions);
        this.nodeComments = nodeComments;
    }

    @Override
    public Node represent(Object data) {
        Node rootNode = super.represent(data);

        if (!(rootNode instanceof MappingNode)) {
            return rootNode;
        }

        for (NodeTuple nodeTuple : ((MappingNode) rootNode).getValue()) {
            if (!(nodeTuple.getKeyNode() instanceof ScalarNode)) {
                continue;
            }

            applyComment((ScalarNode) nodeTuple.getKeyNode(), ((ScalarNode) nodeTuple.getKeyNode()).getValue());

            if (nodeTuple.getValueNode() instanceof MappingNode) {
                String key = ((ScalarNode) nodeTuple.getKeyNode()).getValue();

                resolveSubNodes(((MappingNode) nodeTuple.getValueNode()), key);
            }
        }

        return rootNode;
    }

    protected void resolveSubNodes(MappingNode mappingNode, String key) {
        for (NodeTuple nodeTuple : mappingNode.getValue()) {
            if (!(nodeTuple.getKeyNode() instanceof ScalarNode)) {
                continue;
            }

            String newKey = key + "." + ((ScalarNode) nodeTuple.getKeyNode()).getValue();

            applyComment((ScalarNode) nodeTuple.getKeyNode(), newKey);

            if (nodeTuple.getValueNode() instanceof MappingNode) {
                resolveSubNodes(((MappingNode) nodeTuple.getValueNode()), newKey);
            }
        }
    }

    protected void applyComment(ScalarNode scalarNode, String key) {
        Supplier<String> innerValue = this.nodeComments.get(key);

        if (innerValue != null) {
            scalarNode.setBlockComments(Collections.singletonList(new CommentLine(new CommentEvent(CommentType.BLOCK, " " + innerValue.get(), null, null))));
        }
    }
}
