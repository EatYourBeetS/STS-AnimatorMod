package eatyourbeets.cards.animatorClassic.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.animatorClassic.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class MamiTomoe extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(MamiTomoe.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged);
    static
    {
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public MamiTomoe()
    {
        super(DATA);

        Initialize(9, 0, 1, 1);
        SetUpgrade(2, 0, 0, 0);
        SetScaling(1, 0, 0);


    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
        GameActions.Bottom.Flash(this);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
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

        magicNumber = baseMagicNumber + count;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.05f * magicNumber);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.SFX("ATTACK_FIRE");
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }

        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.MED));
    }
}
