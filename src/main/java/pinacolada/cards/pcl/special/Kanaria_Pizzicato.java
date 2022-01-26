package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Air;
import pinacolada.utilities.PCLActions;

public class Kanaria_Pizzicato extends PCLCard
{
    public static final PCLCardData DATA = Register(Kanaria_Pizzicato.class)
    		.SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.None);

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
        PCLActions.Bottom.DiscardFromHand(name, 1, false).AddCallback(() -> PCLActions.Bottom.Draw(1).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                switch (c.type) {
                    case ATTACK:
                        PCLActions.Bottom.ApplyPoison(TargetHelper.Enemies(), magicNumber);
                        break;
                    case SKILL:
                        PCLActions.Bottom.ChannelOrb(new Air());
                        break;
                    default:
                        PCLActions.Bottom.AddAffinity(PCLAffinity.Green, secondaryValue);
                }
            }
        }));
    }
}