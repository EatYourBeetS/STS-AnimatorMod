package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Venti extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(2).SetSeriesFromClassPackage();

    public Venti()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Star(2, 0, 0);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AbstractOrb orb = new Aether();
        GameActions.Bottom.ChannelOrb(orb);

        GameActions.Bottom.Cycle(name, magicNumber).SetOptions(true, true, true).AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (GameUtilities.IsHindrance(card) || card.type == CardType.ATTACK)
                {
                    GameActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
                }
                else {
                    GameActions.Bottom.TriggerOrbPassive(1).SetFilter(o -> Aether.ORB_ID.equals(o.ID));
                }
            }
        });

    }

}