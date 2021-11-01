package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Ain extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ain.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetMultiformData(2, false)
            .SetSeries(CardSeries.Elsword);

    public Ain()
    {
        super(DATA);

        Initialize(2, 0, 1, 3);
        SetUpgrade(1, 0, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(2, 0, 2);

        SetAffinityRequirement(Affinity.Light, 5);

        SetHitCount(3);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (GameUtilities.GetOrbCount(Frost.ORB_ID) > 0) {
            return super.ModifyDamage(enemy, amount + magicNumber);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            LoadImage("_Dark");
            affinities.Clear();
            SetAffinity_Blue(2, 0, 2);
            SetAffinity_Dark(1);
            SetAffinity_Light(0);
            SetAffinityRequirement(Affinity.Dark, 4);
            SetAffinityRequirement(Affinity.Light, 0);
        }
        else {
            this.cardText.OverrideDescription(null, true);
            affinities.Clear();
            SetAffinity_Blue(2, 0, 2);
            SetAffinity_Dark(0);
            SetAffinity_Light(1);
            SetAffinityRequirement(Affinity.Dark, 0);
            SetAffinityRequirement(Affinity.Light, 4);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        //GameActions.Bottom.VFX(new BlizzardEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.6f);
        GameActions.Bottom.Callback(() ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + hitCount + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f - (float)frostCount / 200f);
            for (int i = 0; i < frostCount; i++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        }

        if ((auxiliaryData.form == 1 && TrySpendAffinity(Affinity.Dark)) || (auxiliaryData.form == 0 && TrySpendAffinity(Affinity.Light)))
        {
            GameActions.Bottom.GainFocus(secondaryValue, true);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameActionManager.totalDiscardedThisTurn > 0;
    }
}