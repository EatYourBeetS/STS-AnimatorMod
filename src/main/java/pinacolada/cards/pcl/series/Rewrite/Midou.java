package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_SearingBurn;
import pinacolada.cards.pcl.status.Status_Burn;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;

public class Midou extends PCLCard
{
    public static final PCLCardData DATA = Register(Midou.class).SetAttack(0, CardRarity.COMMON, PCLAttackType.Fire, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_SearingBurn(), false).AddPreview(new Status_Burn(), false));

    public Midou()
    {
        super(DATA);

        Initialize(2, 0, 1, 1);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID)) {

            PCLActions.Bottom.ExhaustFromPile(name, magicNumber, player.drawPile, player.hand, player.discardPile)
                    .SetOptions(true, true)
                    .SetFilter(c -> c instanceof Status_Burn);
        }

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.FIRE);

        PCLActions.Bottom.ChannelOrb(new Fire());
        PCLActions.Bottom.MakeCardInHand(new Status_Burn());
    }
}