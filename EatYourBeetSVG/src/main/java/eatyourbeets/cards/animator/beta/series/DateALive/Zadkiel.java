package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.EvokeAllOrbsAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zadkiel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zadkiel.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None);

    public Zadkiel()
    {
        super(DATA);

        Initialize(0, 4, 4);
        SetCostUpgrade(-1);

        SetExhaust(true);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Add(new EvokeAllOrbsAction());

        int frostCount = JUtils.Count(player.orbs, orb -> Frost.ORB_ID.equals(orb.ID));
        if (frostCount >= magicNumber)
        {
            GameActions.Bottom.Callback(() ->
            {
                int[] damageMatrix = DamageInfo.createDamageMatrix(player.currentBlock);
                GameActions.Bottom.VFX(new WhirlwindEffect());
                GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
                GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            });
        }
    }
}