package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.CounterAttackPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class Nanami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nanami.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Nanami()
    {
        super(DATA);

        Initialize(0, 6, 2, 4);
        SetUpgrade(0, 2, 1);

        SetAffinity_Red(2);
        SetAffinity_Green(2, 0, 2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (JUtils.Find(GameUtilities.GetIntents(), EnemyIntent::IsAttacking) != null && CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.Cycle(name, 1).AddCallback(cards -> {
               for (AbstractCard c : cards) {
                   if (GameUtilities.HasGreenAffinity(c)) {
                       GameActions.Bottom.StackPower(new CounterAttackPower(player, magicNumber));
                   }
               }
            });
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        for (EnemyIntent intent : GameUtilities.GetIntents()) {
            if (intent.IsAttacking()) {
                GameActions.Bottom.ApplyWeak(TargetHelper.Normal(intent.enemy), magicNumber);
            }
            else {
                GameActions.Bottom.ApplyVulnerable(TargetHelper.Normal(intent.enemy), magicNumber);
            }
        }

        if (CheckPrimaryCondition(true)) {
            GameActions.Bottom.GainTemporaryThorns(secondaryValue);
        }
        else {
            GameActions.Bottom.StackPower(new CounterAttackPower(p, secondaryValue));
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return JUtils.Count(GameUtilities.GetIntents(), EnemyIntent::IsAttacking) >= GameUtilities.GetIntents().size() / 2;
    }
}