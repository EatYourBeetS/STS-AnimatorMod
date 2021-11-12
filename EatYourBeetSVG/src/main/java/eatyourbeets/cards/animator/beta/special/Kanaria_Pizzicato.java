package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Kanaria_Pizzicato extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kanaria_Pizzicato.class)
    		.SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public Kanaria_Pizzicato()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false).AddCallback(() -> GameActions.Bottom.Draw(1).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                switch (c.type) {
                    case ATTACK:
                        GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), magicNumber);
                        break;
                    case SKILL:
                        GameActions.Bottom.ChannelOrb(new Air());
                        break;
                    default:
                        GameActions.Bottom.AddAffinity(Affinity.Green, secondaryValue);
                }
            }
        }));
    }
}