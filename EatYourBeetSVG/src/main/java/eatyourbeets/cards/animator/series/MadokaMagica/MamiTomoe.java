package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.curse.MamiTomoe_Candeloro;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MamiTomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MamiTomoe.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new MamiTomoe_Candeloro(), false);
                data.AddPreview(new Curse_GriefSeed(), false);});

    public MamiTomoe()
    {
        super(DATA);

        Initialize(9, 0, 1, 1);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Orange(1);
        SetAffinity_Light(2, 0, 2);
        SetSoul(3, 1, MamiTomoe_Candeloro::new);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int count = 0;
        String id = Curse_GriefSeed.DATA.ID;
        if (player.drawPile.findCardById(id) != null)
        {
            count += 1;
        }
        if (player.discardPile.findCardById(id) != null)
        {
            count += 1;
        }
        if (player.exhaustPile.findCardById(id) != null)
        {
            count += 1;
        }

        GameUtilities.IncreaseHitCount(this, count, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.05f * magicNumber);
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);

        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.MED));

        cooldown.ProgressCooldownAndTrigger(m);
    }
}
