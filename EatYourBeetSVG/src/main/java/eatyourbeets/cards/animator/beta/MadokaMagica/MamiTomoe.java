package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class MamiTomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MamiTomoe.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged);
    static
    {
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public MamiTomoe()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0, 0);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = player.discardPile.getCardsOfType(CardType.CURSE).size() + player.drawPile.getCardsOfType(CardType.CURSE).size() + 1;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.discardPile.getCardsOfType(CardType.CURSE).isEmpty())
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
        }

        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.05f * magicNumber);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.SFX("ATTACK_FIRE");
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        }

        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.MED));
    }
}
