package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YamaiSisters extends PCLCard implements OnSynergySubscriber
{
    public static final PCLCardData DATA = Register(YamaiSisters.class).SetAttack(0, CardRarity.COMMON, PCLAttackType.Normal).SetSeriesFromClassPackage();

    public YamaiSisters()
    {
        super(DATA);

        Initialize(1, 0, 3);
        SetUpgrade(0, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetHitCount(2,0);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onSynergy.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (IsStarter())
        {
            PCLActions.Bottom.MakeCardInHand(PCLGameUtilities.Imitate(this));
        }
    }

    @Override
    public void OnSynergy(AbstractCard card) {
        if (PCLGameUtilities.GetCurrentMatchCombo() >= magicNumber && CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Last.MoveCard(this,player.hand).ShowEffect(true, true);
        }
    }
}