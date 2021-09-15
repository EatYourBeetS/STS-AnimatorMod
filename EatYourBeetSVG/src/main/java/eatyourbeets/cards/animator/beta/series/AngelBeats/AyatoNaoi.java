package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class AyatoNaoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AyatoNaoi.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage();

    public AyatoNaoi()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);

        if (CombatStats.ControlPile.Contains(this))
        {
            GameActions.Bottom.Callback(() ->
            {
                int totalDamage = 0;
                for (AbstractMonster mo : GameUtilities.GetEnemies(true))
                {
                    totalDamage += GameUtilities.GetIntent(mo).GetDamage(true);
                }

                if (totalDamage > 0)
                {
                    int[] newMultiDamage = DamageInfo.createDamageMatrix(totalDamage, true);
                    GameActions.Top.Add(new VFXAction(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F));
                    GameActions.Top.Add(new DamageAllEnemiesAction(player, newMultiDamage, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE));
                }
            });
        }

        //Put this last to be more player-friendly aka dark orbs won't kill an enemy that might have contributed
        //to the above effect's damage
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.ChannelOrb(new Dark());
        }
    }
}