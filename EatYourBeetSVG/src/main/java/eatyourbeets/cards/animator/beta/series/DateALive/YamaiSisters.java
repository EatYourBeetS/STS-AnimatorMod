package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YamaiSisters extends AnimatorCard implements OnSynergySubscriber
{
    public static final EYBCardData DATA = Register(YamaiSisters.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    public YamaiSisters()
    {
        super(DATA);

        Initialize(1, 0 );
        SetUpgrade(0, 0 );
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetHitCount(2,0);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        CombatStats.onSynergy.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (IsStarter())
        {
            GameActions.Bottom.MakeCardInHand(makeStatEquivalentCopy());
        }
    }

    @Override
    public void OnSynergy(AbstractCard card) {
        if ((GameUtilities.HasRedAffinity(card) || GameUtilities.HasLightAffinity(card)) && CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Last.MoveCard(this,player.hand);
        }
    }
}