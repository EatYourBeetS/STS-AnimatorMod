package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.utilities.GameActions;

public class Kanaria_Pizzicato extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kanaria_Pizzicato.class)
    		.SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public Kanaria_Pizzicato()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this).SetText("1", Settings.CREAM_COLOR);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(1);
        boolean hasAether = false;
        for (AbstractOrb orb : p.orbs)
            if (Air.ORB_ID.equals(orb.ID))
            {
                hasAether = true;
                break;
            }

        if (hasAether)
            GameActions.Bottom.Draw(magicNumber);
        else
            GameActions.Bottom.ChannelOrb(new Air());

        GameActions.Bottom.DiscardFromHand(name, 1, false);
    }
}

// If you have Aether Orb: Draw !M! cards, then discard 1 card. Otherwise: Channel 1 Aether.