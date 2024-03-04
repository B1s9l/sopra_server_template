package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-04T11:25:35+0100",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.10 (Eclipse Adoptium)"
)
public class DTOMapperImpl implements DTOMapper {

    @Override
    public User convertUserPostDTOtoEntity(UserPostDTO userPostDTO) {
        if ( userPostDTO == null ) {
            return null;
        }

        User user = new User();

        user.setBirthday( userPostDTO.getBirthday() );
        user.setPassword( userPostDTO.getPassword() );
        user.setUserId( userPostDTO.getUserId() );
        user.setToken( userPostDTO.getToken() );
        user.setUsername( userPostDTO.getUsername() );
        user.setStatus( userPostDTO.getStatus() );
        user.setCreationDate( userPostDTO.getCreationDate() );

        return user;
    }

    @Override
    public UserGetDTO convertEntityToUserGetDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserGetDTO userGetDTO = new UserGetDTO();

        userGetDTO.setBirthday( user.getBirthday() );
        userGetDTO.setPassword( user.getPassword() );
        userGetDTO.setCreationDate( user.getCreationDate() );
        userGetDTO.setUserId( user.getUserId() );
        userGetDTO.setToken( user.getToken() );
        userGetDTO.setUsername( user.getUsername() );
        userGetDTO.setStatus( user.getStatus() );

        return userGetDTO;
    }
}
