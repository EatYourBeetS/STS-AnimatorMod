package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.stances.pcl.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Ain extends PCLCard
{
    public static final PCLCardData DATA = Register(Ain.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Ice, PCLCardTarget.AoE)
            .SetMultiformData(2, false)
            .SetSeries(CardSeries.Elsword);

    public Ain()
    {
        super(DATA);

        Initialize(2, 0, 2, 3);
        SetUpgrade(1, 0, 1, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 5);

        SetHitCount(3);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            LoadImage("_Dark");
            affinities.Clear();
            SetAffinity_Blue(1, 0, 2);
            SetAffinity_Dark(1);
            SetAffinity_Light(0);
            SetAffinityRequirement(PCLAffinity.Dark, 4);
            SetAffinityRequirement(PCLAffinity.Light, 0);
        }
        else {
            this.cardText.OverrideDescription(null, true);
            affinities.Clear();
            SetAffinity_Blue(1, 0, 2);
            SetAffinity_Dark(0);
            SetAffinity_Light(1);
            SetAffinityRequirement(PCLAffinity.Dark, 0);
            SetAffinityRequirement(PCLAffinity.Light, 4);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        //GameActions.Bottom.VFX(new BlizzardEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.6f);
        PCLActions.Bottom.Callback(() ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + hitCount + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f - (float)frostCount / 200f);
            for (int i = 0; i < frostCount; i++)
            {
                PCLGameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });

        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false));

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
        }

        if ((auxiliaryData.form == 1 && TrySpendAffinity(PCLAffinity.Dark)) || (auxiliaryData.form == 0 && TrySpendAffinity(PCLAffinity.Light)))
        {
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Sorcery, secondaryValue);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameActionManager.totalDiscardedThisTurn > 0 || PCLGameUtilities.GetOrbCount(Frost.ORB_ID) >= 2;
    }
}