package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Saber_Excalibur;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Saber extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Saber.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Normal, EYBCardTarget.Normal, true, true)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Saber_Excalibur(), false));

    public Saber()
    {
        super(DATA);

        Initialize(9, 4, 0);
        SetUpgrade(2, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(2, 0, 1);

        SetCooldown(8, 0, this::OnCooldownCompleted);
        SetLoyal(true);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return GetInitialBlock() > 0 ? super.GetBlockInfo() : null;
    }

    @Override
    protected float GetInitialBlock()
    {
        ArrayList<EnemyIntent> intents = GameUtilities.GetIntents();
        if (intents.size() == 0) {
            return 0;
        }
        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            if (!intent.IsAttacking())
            {
                return 0;
            }
        }
        return super.GetInitialBlock();
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainRandomAffinityPower(1,false, Affinity.Light,Affinity.Red);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        if (block > 0) {
            GameActions.Bottom.GainBlock(block);
        }

        cooldown.ProgressCooldownAndTrigger(info.IsSynergizing ? 3 : 1, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Purge(uuid);
        GameActions.Bottom.MakeCardInHand(new Saber_Excalibur());
    }
}